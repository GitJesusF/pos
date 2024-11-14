package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("roles")
public class RoleList extends Div{

  //Componentes
  private final Grid<Role> grid;
  private final TextField txtSearchField;
  private final Button btnInsert;

  @Autowired
  private RoleService roleService;

  public RoleList() {
    //Grid usuarios
    grid = new Grid<>(Role.class, false);
    grid.addItemDoubleClickListener(event -> {
      event.getSource().getUI().ifPresent(ui -> ui.navigate(RoleForm.class, event.getItem().getId()));
      //Notification.show(event.getItem().getId().toString());
    });
    grid.addColumn(Role::getName).setHeader("Rol").setSortable(true).setFlexGrow(1);


    // Configuración de busqueda
    txtSearchField = new TextField();
    txtSearchField.setPlaceholder("Search");
    txtSearchField.setClearButtonVisible(true);
    txtSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    txtSearchField.setValueChangeMode(ValueChangeMode.EAGER);
    txtSearchField.addValueChangeListener(e -> refresh());

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
    btnInsert.getUI().ifPresent(ui -> ui.navigate(RoleForm.class));
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    List<Role> items = roleService.getAllRoles();
    grid.setItems(items);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
  }

  private void refresh() {
    //trim() ignora los espacios en blanco en la busqueda
    //stream() manipula

    String sSearchTerm = txtSearchField.getValue().trim().toLowerCase();

    List<Role> allRoles = roleService.getAllRoles();
    if (sSearchTerm.isEmpty()) {
      grid.setItems(roleService.getAllRoles());
    } else {
//      grid.setItems(allRoles.stream()
//          .filter(Role -> matchesTerm(
//              Role.getName(), searchTerm))
//          .toList());
      // filtrar los registros en la base de datos
      List<Role> items = roleService.findByFilter(sSearchTerm);
      grid.setItems(items);
    }
  }
}