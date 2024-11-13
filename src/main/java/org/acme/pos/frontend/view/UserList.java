package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.acme.pos.backend.entity.User;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Route("users")
public class UserList extends Div{

  //Componentes
  private final Grid<User> grid;
  private final TextField txtSearchField;
  private final Button btnInsert;

  @Autowired
  private UserService userService;


  public UserList() {
    //Grid usuarios
    grid = new Grid<>(User.class, false);
    grid.addItemDoubleClickListener(event -> {
      event.getSource().getUI().ifPresent(ui -> ui.navigate(UserForm.class, event.getItem().getId()));
      //Notification.show(event.getItem().getId().toString());
    });
    grid.addColumn(User::getFirstName).setHeader("Nombre").setSortable(true).setFlexGrow(1);
    grid.addColumn(User::getLastName).setHeader("Apellido").setSortable(true).setFlexGrow(1);
    grid.addColumn(User::getUsername).setHeader("Usuario").setFlexGrow(1);

    grid.addColumn(user -> user.getRole() != null ? user.getRole().getRole() : "Sin rol").setHeader("Rol").setSortable(true).setFlexGrow(1);

    grid.addColumn(User::getEmail).setHeader("Email").setFlexGrow(1).setAutoWidth(true);

    // Configuración de busqueda
    txtSearchField = new TextField();
    txtSearchField.setPlaceholder("Search");
    txtSearchField.setClearButtonVisible(true);
    txtSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    txtSearchField.setValueChangeMode(ValueChangeMode.EAGER);
    txtSearchField.addValueChangeListener(e -> filterUsers());

    //Botón de agregación
    btnInsert = new Button(VaadinIcon.PLUS.create());
    btnInsert.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnInsert.addClickListener(event -> openForm());

    //Encabezado
    HorizontalLayout header = new HorizontalLayout();
    header.setWidthFull();
    header.setPadding(true);
    header.addClassNames("items-center", "justify-between");
    header.add(txtSearchField, btnInsert);

    add(header, grid);
  }

  private void openForm() {
    btnInsert.getUI().ifPresent(ui -> ui.navigate(UserForm.class));
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    List<User> items = userService.getAllUsers();
    grid.setItems(items);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
  }

  private void filterUsers() {

    //trim() ignora los espacios en blanco en la busqueda
    //stream() manipula

    String searchTerm = txtSearchField.getValue().trim().toLowerCase();

    List<User> allUsers = userService.getAllUsers();

    if (searchTerm.isEmpty()) {
      grid.setItems(allUsers);
    } else {
      grid.setItems(allUsers.stream()
          .filter(user -> matchesTerm(
              user.getFirstName(), searchTerm) ||
              matchesTerm(user.getLastName(), searchTerm) ||
              matchesTerm(user.getUsername(), searchTerm) ||
              matchesTerm(user.getRole().getRole(), searchTerm) ||
              matchesTerm(user.getEmail(), searchTerm))
          .toList());
    }
  }

  private boolean matchesTerm(String value, String searchTerm) {
    return value != null && value.toLowerCase().contains(searchTerm);
  }
}