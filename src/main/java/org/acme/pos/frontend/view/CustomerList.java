package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import org.acme.pos.backend.entity.Customer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Route("customers")
public class CustomerList extends Div{

  //Componentes
  private Sort sort;
  private final Grid<Customer> grid;

  private final TextField txtSearchField;
  private final Button btnInsert;

  @Autowired
  private CustomerService customerService;

  public CustomerList() {
    //Grid clientes
    grid = new Grid<>(Customer.class, false);
    grid.addItemDoubleClickListener(event -> {
      event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerForm.class, event.getItem().getId()));
      //Notification.show(event.getItem().getId().toString());
    });
    grid.addSortListener(event -> {
      for (GridSortOrder<Customer> so : event.getSortOrder()) {
        if (so.getSorted().getKey() != null) {
          if (so.getDirection().equals(SortDirection.ASCENDING)) {
            sort = Sort.by(so.getSorted().getKey());
          } else {
            sort = Sort.by(so.getSorted().getKey()).descending();
          }
          refresh(0);
          System.out.println("> Column clicked for sort: " + so.getSorted().getKey() + " (" + so.getDirection() + ")");
        }
      }
    });

    grid.addColumn(Customer::getLastName).setKey("lastName").setHeader("Apellido").setSortable(true).setFlexGrow(1);
    grid.addColumn(Customer::getFirstName).setKey("firstName").setHeader("Nombre").setSortable(true).setFlexGrow(1);
    grid.addColumn(Customer::getEmail).setHeader("Email").setFlexGrow(2);
    grid.addColumn(Customer::getPhone).setHeader("Teléfono ").setFlexGrow(0).setAutoWidth(true);

    // Configuración de busqueda
    txtSearchField = new TextField();
    txtSearchField.setPlaceholder("Search");
    txtSearchField.setClearButtonVisible(true);
    txtSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    txtSearchField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
    txtSearchField.addValueChangeListener(e -> refresh(0));

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
    btnInsert.getUI().ifPresent(ui -> ui.navigate(CustomerForm.class));
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    // sort by default
    sort = Sort.by("lastName", "firstName");
    refresh(0);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
  }

  private void refresh(int iPage) {
    PageRequest pageRequest = PageRequest.of(iPage, 2, sort);
    //trim() ignora los espacios en blanco en la busqueda
    //stream() manipula
    String sSearchTerm = txtSearchField.getValue().trim().toLowerCase();

    if (sSearchTerm.isEmpty()) {
      Page<Customer> page = customerService.getAllCustomers(pageRequest);
      System.out.println("> page size: "+page.getSize());
      System.out.println("> page number: "+page.getNumber());
      System.out.println("> page numberElements: "+page.getNumberOfElements());
      System.out.println("> page totalPages: "+page.getTotalPages());
      System.out.println("> page totalElements: "+page.getTotalElements());
      grid.setItems(page.getContent());
    } else {
//      // filtrar solamente con los registros obtenidos de la base de datos
//      grid.setItems(allCustomers.stream()
//          .filter(customer -> matchesTerm(
//              customer.getFirstName(), sSearchTerm) ||
//              matchesTerm(customer.getLastName(), sSearchTerm) ||
//              matchesTerm(customer.getEmail(), sSearchTerm) ||
//              matchesTerm(customer.getPhone(), sSearchTerm))
//          .toList());

      // filtrar los registros en la base de datos
      List<Customer> items = customerService.findByFilter(sSearchTerm, sort);
      grid.setItems(items);
    }
  }
}