package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.Item;
import org.acme.pos.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Route(value = "items", layout = MainLayout.class)
@PageTitle("Item List")
public class ItemList extends VerticalLayout {

  //Componentes
  private  TextField txtSearchField;
  private  Button btnInsert;

  private Sort sort;
  private Page<Item> page;
  private Grid<Item> grid;

  //Componentes de paginacion
  private Div divDisplayRecordText;
  private Button btnFirstPage;
  private Button btnPreviousPage;
  private IntegerField txtDesiredPage;
  private Button btnNextPage;
  private Button btnLastPage;
  private ComboBox<Integer> cbxRecordsByPage;

  @Autowired
  private ItemService itemService;

  public ItemList() {
    this.setSizeFull();

    HorizontalLayout toolbar = doCreateToolbar();
    Grid<Item> grid = doCreateGrid();
    HorizontalLayout footer = doCreateFooter();

    this.add(toolbar, grid, footer);
    this.addClassName("content-start");
    this.addClassName("items-center");
  }

  private void openForm() {
    btnInsert.getUI().ifPresent(ui -> ui.navigate(ItemForm.class));
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    // sort by default
    sort = Sort.by("code", "name");
    refresh(0);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
  }

  private HorizontalLayout doCreateToolbar() {
    // Configuración de busqueda
    txtSearchField = new TextField();
    txtSearchField.setPlaceholder("Buscar");
    txtSearchField.setClearButtonVisible(true);
    txtSearchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    txtSearchField.setValueChangeMode(ValueChangeMode.EAGER);
    txtSearchField.addValueChangeListener(e -> refresh(0));

    //Botón de agregación
    btnInsert = new Button(VaadinIcon.PLUS.create());
    btnInsert.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnInsert.addClickListener(event -> openForm());

    //Encabezado
    HorizontalLayout toolbar = new HorizontalLayout();
    toolbar.setWidthFull();
    toolbar.addClassNames("items-center", "justify-between");
    toolbar.add(txtSearchField, btnInsert);

    return toolbar;
  }

  private Grid<Item> doCreateGrid() {
    grid = new Grid<>(Item.class, false);
    grid.setSizeFull();
    grid.addItemDoubleClickListener(event -> event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerForm.class, event.getItem().getId())));

    grid.addSortListener(event -> {
      for (GridSortOrder<Item> so : event.getSortOrder()) {
        if (so.getSorted().getKey() != null) {
          if (so.getDirection().equals(SortDirection.ASCENDING)) {
            sort = Sort.by(so.getSorted().getKey());
          } else {
            sort = Sort.by(so.getSorted().getKey()).descending();
          }
          refresh(0);
          // System.out.println("> Column clicked for sort: " + so.getSorted().getKey() + " (" + so.getDirection() + ")");
        }
      }
    });

    grid.addColumn(Item::getCode).setHeader("Código").setSortable(true).setFlexGrow(1);
    grid.addColumn(Item::getName).setHeader("Nombre").setSortable(true).setFlexGrow(1);
    grid.addColumn(Item::getPrice).setHeader("Precio").setSortable(true).setFlexGrow(1);

    return grid;
  }

  private HorizontalLayout doCreateFooter() {

    divDisplayRecordText = new Div();
    divDisplayRecordText.setText("Mostrando 0 de 0 registros");


    btnFirstPage = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    btnFirstPage.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    btnFirstPage.addClickListener(event -> {
      refresh(0);
    });

    btnPreviousPage = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    btnPreviousPage.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    btnPreviousPage.addClickListener(event -> {
      if (page.getNumber() > 0) {
        refresh(page.getNumber() - 1);
      }
    });

    btnNextPage = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    btnNextPage.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    btnNextPage.addClickListener(event -> {
      if (page.getNumber() < page.getTotalPages() - 1) {
        refresh(page.getNumber() + 1);
      }
    });

    btnLastPage = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    btnLastPage.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    btnLastPage.addClickListener(event -> {
      refresh(page.getTotalPages() - 1);
    });


    txtDesiredPage = new IntegerField();
    txtDesiredPage.setWidth("60px");
    txtDesiredPage.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
    txtDesiredPage.addValueChangeListener(event -> {
      // Asegurarse de que el valor no sea nulo o negativo y que no supere el total de páginas
      if (event.getValue() != null && event.getValue() > 0 && event.getValue() <= page.getTotalPages()) {
        // Convertir el valor de página a índice basado en 0
        refresh(event.getValue() - 1);
      } else {
        Notification.show("Error");
      }
    });

    Button btnPageConfig = new Button(VaadinIcon.COG.create());
    btnPageConfig.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
    btnPageConfig.addClickListener(event -> openPageConfigDialog());

    cbxRecordsByPage = new ComboBox<>("Elementos a mostrar");
    cbxRecordsByPage.setItems(2, 5, 10, 20);
    cbxRecordsByPage.setValue(2);

    HorizontalLayout footer = new HorizontalLayout(
        new FlexLayout(btnPageConfig, divDisplayRecordText),
        new FlexLayout(btnFirstPage, btnPreviousPage, txtDesiredPage, btnNextPage, btnLastPage));
    footer.setWidthFull();
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    footer.addClassName("items-center");

    return footer;
  }

  private void openPageConfigDialog() {
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setHeader("Configurar elementos por página");
    dialog.add(cbxRecordsByPage);
    dialog.addConfirmListener(event -> refresh(0));

    dialog.open();
  }



  private void refresh(int iPage) {

    int pageSize = cbxRecordsByPage.getValue();
    PageRequest pageRequest = PageRequest.of(iPage, pageSize, sort);
    String sSearchTerm = txtSearchField.getValue().trim().toLowerCase();

    if (sSearchTerm.isEmpty()) {
      page = itemService.getAllItems(pageRequest);
//      System.out.println("> page size: "+page.getSize());
//      System.out.println("> page number: "+page.getNumber());
//      System.out.println("> page numberElements: "+page.getNumberOfElements());
//      System.out.println("> page totalPages: "+page.getTotalPages());
//      System.out.println("> page totalElements: "+page.getTotalElements());
    } else {
      page = itemService.findByFilter(sSearchTerm, pageRequest);
    }

    grid.setItems(page.getContent());
    txtDesiredPage.setValue(page.getNumber() + 1);

    if (page.getNumberOfElements() > 0) {
      divDisplayRecordText.setText("Mostrando " + page.getNumberOfElements() + " de " + page.getTotalElements() + " registros");
    } else {
      divDisplayRecordText.setText("No se encontraron registros");
    }

    btnFirstPage.setEnabled(page.getNumber() > 0);
    btnPreviousPage.setEnabled(page.getNumber() > 0);

    btnNextPage.setEnabled(page.getNumber() < page.getTotalPages() - 1);
    btnLastPage.setEnabled(page.getNumber() < page.getTotalPages() - 1);
  }
}