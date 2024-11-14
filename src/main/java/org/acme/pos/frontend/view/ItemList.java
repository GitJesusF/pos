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
import org.acme.pos.backend.entity.Item;
import org.acme.pos.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("items")
public class ItemList extends Div{

  //Componentes
  private final Grid<Item> grid;
  private final TextField txtSearchField;
  private final Button btnInsert;

  @Autowired
  private ItemService itemService;

  public ItemList() {
    //Grid usuarios
    grid = new Grid<>(Item.class, false);
    grid.addItemDoubleClickListener(event -> {
      event.getSource().getUI().ifPresent(ui -> ui.navigate(ItemForm.class, event.getItem().getId()));
      //Notification.show(event.getItem().getId().toString());
    });
    grid.addColumn(Item::getCode).setHeader("Código").setSortable(true).setFlexGrow(1);
    grid.addColumn(Item::getName).setHeader("Nombre").setSortable(true).setFlexGrow(1);
    grid.addColumn(Item::getPrice).setHeader("Precio").setSortable(true).setFlexGrow(1);

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
    btnInsert.getUI().ifPresent(ui -> ui.navigate(ItemForm.class));
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    List<Item> items = itemService.getAllItems();
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

    if (sSearchTerm.isEmpty()) {
      grid.setItems(itemService.getAllItems());
    } else {
//      // filtrar solamente con los registros obtenidos de la base de datos
//        grid.setItems(allItems.stream()
//            .filter(Item -> matchesTerm(
//                Item.getCode(), searchTerm) ||
//                matchesTerm(Item.getName(), searchTerm) ||
//                matchesTerm(Item.getPrice().toString(), searchTerm))
//            .toList());

      // filtrar los registros en la base de datos
      List<Item> items = itemService.findByFilter(sSearchTerm);
      grid.setItems(items);
    }
  }
}