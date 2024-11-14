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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("role")
public class RoleForm extends VerticalLayout implements HasUrlParameter<Integer> {

  private Role data;
  private final Binder<Role> binder = new Binder<>(Role.class);

  private TextField txtRole;
  private Button btnDelete;
  private Button btnCancel;
  private Button btnConfirm;

  @Autowired
  private RoleService roleService;

  public RoleForm() {

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
    binder.forField(txtRole)
        .asRequired("El rol es obligatorio")
        .bind(Role::getName, Role::setName);

    binder.readBean(data);

    txtRole.focus();
  }

  private HorizontalLayout doCreateHeader(){
    // Encabezado y diseño
    HorizontalLayout header = new HorizontalLayout();
    header.setId("header");
    header.getThemeList().set("dark", true);
    header.setWidthFull();
    header.setSpacing(false);
    header.addClassName("items-center");
    header.add(new DrawerToggle());
    H1 viewTitle = new H1("Sección de roles");
    header.add(viewTitle);
    return header;
  }

  private FormLayout doCreateForm() {
    // Componentes
    txtRole = new TextField("Rol");
    txtRole.setRequiredIndicatorVisible(true);


    // Contenedor del formulario
    FormLayout formLayout = new FormLayout();
    formLayout.add(txtRole);

    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
    formLayout.addClassName("p-m");
    return formLayout;
  }


  private HorizontalLayout doCreateActionButtons(){
    // Botones
    btnCancel = new Button("Cancelar");
    btnCancel.addClickListener(event ->
        event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class)));

    btnConfirm = new Button("Aceptar");
    btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnConfirm.addFocusShortcut(Key.ENTER).listenOn(txtRole);
    btnConfirm.addClickListener(event -> {
      // This makes all current validation errors visible.
      BinderValidationStatus<Role> status = binder.validate();
      if (status.hasErrors()) {
        //Notification.show(status.getValidationErrors().toString(), 5000, Notification.Position.MIDDLE);
        return;
      }

      try {
        binder.writeBean(data);
        roleService.saveRole(data);
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
    dialog.setHeader("¿Está muy seguro de eliminar este rol?");
    dialog.setText("Una vez eliminado, no se podrá recuperar la información.");
    dialog.setCancelText("No");
    dialog.setCancelable(true);
    dialog.setConfirmText("Sí");
    dialog.setConfirmButtonTheme("error primary");
    dialog.addConfirmListener(event -> {
      roleService.deleteRoleById(data.getId());
      event.getSource().getUI().ifPresent(ui -> ui.navigate(ItemList.class));
    });
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
    if (id != null) {
      data = roleService.getRoleById(id).orElse(new Role());
      btnDelete.setEnabled(true);
      btnDelete.setVisible(true);
    } else {
      data = new Role();
      btnDelete.setEnabled(false);
      btnDelete.setVisible(false);
    }
  }
}
