package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Item;
import org.acme.pos.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("item")
public class ItemForm extends VerticalLayout implements HasUrlParameter<Integer> {

  private Item data;
  private final Binder<Item> binder = new Binder<>(Item.class);

  private TextField txtCode;
  private TextField txtName;
  private BigDecimalField txtPrice;
  private Button btnDelete;
  private Button btnConfirm;
  private Button btnCancel;

  @Autowired
  private ItemService itemService;

  public ItemForm() {

    HorizontalLayout header = doCreateHeader();
    FormLayout form = doCreateForm();
    HorizontalLayout layAction = doCreateActionButtons();

    this.add(header, form, layAction);
    this.addClassName("content-start");
    this.addClassName("items-center");
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    // Formulario y validación con Binder
    binder.forField(txtCode)
        .asRequired("El código es obligatorio")
        .bind(Item::getCode, Item::setCode);

    binder.forField(txtName)
        .asRequired("El nombre es obligatorio")
        .bind(Item::getName, Item::setName);

    binder.forField(txtPrice)
        .asRequired("El precio es obligatorio")
        .bind(Item::getPrice, Item::setPrice);


    binder.readBean(data);
    txtCode.focus();
  }

  private HorizontalLayout doCreateHeader(){
    // Encabezado
    HorizontalLayout header = new HorizontalLayout();
    header.setId("header");
    header.getThemeList().set("dark", true);
    header.setWidthFull();
    header.setSpacing(false);
    header.addClassName("items-center");
    header.add(new DrawerToggle());
    H1 viewTitle = new H1("Sección de items");
    header.add(viewTitle);
    return header;
  }

  private FormLayout doCreateForm() {
    // Componentes
    txtCode = new TextField("Código");
    txtName = new TextField("Nombre");
    txtPrice = new BigDecimalField("Precio");

    txtCode.setRequiredIndicatorVisible(true);

    txtName.setRequiredIndicatorVisible(true);
    txtName.addFocusShortcut(Key.ENTER).listenOn(txtCode);

    txtPrice.setRequiredIndicatorVisible(true);
    txtPrice.addFocusShortcut(Key.ENTER).listenOn(txtName);


    // Contenedor del formulario
    FormLayout formLayout = new FormLayout();
    formLayout.add(txtCode, txtName, txtPrice);
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
    formLayout.addClassName("p-m");
    formLayout.add(txtCode, txtName, txtPrice);

    return formLayout;
  }

  private HorizontalLayout doCreateActionButtons(){
    // Botones

    btnDelete = new Button(new Icon(VaadinIcon.TRASH));
    btnDelete.addClassNames("bg-error", "text-error-contrast");
    btnDelete.addClickListener(event -> openConfirmDeleteDialog());

    btnCancel = new Button("Cancelar");

    btnConfirm = new Button("Aceptar");
    btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnConfirm.addFocusShortcut(Key.ENTER).listenOn(txtPrice);
    btnConfirm.addClickListener(event -> {
      // This makes all current validation errors visible.
      BinderValidationStatus<Item> status = binder.validate();
      if (status.hasErrors()) {
        //Notification.show(status.getValidationErrors().toString(), 5000, Notification.Position.MIDDLE);
        return;
      }

      try {
        binder.writeBean(data);
        itemService.saveItem(data);
        event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class));
      } catch (ValidationException e) {
        e.printStackTrace();
      }
    });

    btnDelete = new Button(new Icon(VaadinIcon.TRASH));
    btnDelete.addClassNames("bg-error", "text-error-contrast","mr-auto");
    btnDelete.addClickListener(event -> openConfirmDeleteDialog());

    // Contenedor de eliminar
    HorizontalLayout layAction = new HorizontalLayout(btnDelete, btnCancel, btnConfirm);
    layAction.setWidthFull();
    layAction.addClassName("justify-end");
    layAction.setPadding(true);

    return layAction;
  }


  private void openConfirmDeleteDialog() {
    // Dialogo de eliminación
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setHeader("¿Está muy seguro de eliminar este item?");
    dialog.setText("Una vez eliminado, no se podrá recuperar la información.");
    dialog.setCancelText("No");
    dialog.setCancelable(true);
    dialog.setConfirmText("Sí");
    dialog.setConfirmButtonTheme("error primary");
    dialog.addConfirmListener(event -> {
      itemService.deleteItemById(data.getId());
      event.getSource().getUI().ifPresent(ui -> ui.navigate(ItemList.class));
    });
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
    if (id != null) {
      data = itemService.getItemById(id).orElse(new Item());
      btnDelete.setEnabled(true);
      btnDelete.setVisible(true);
    } else {
      data = new Item();
      btnDelete.setEnabled(false);
      btnDelete.setVisible(false);
    }
  }
}
