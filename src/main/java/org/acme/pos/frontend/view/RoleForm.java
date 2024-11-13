package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
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
public class RoleForm extends Div implements HasUrlParameter<Integer> {

  private Role data;
  private final Binder<Role> binder = new Binder<>(Role.class);

  private TextField txtRole;
  private Button btnDelete;
  private Button btnCancel;
  private Button btnConfirm;

  @Autowired
  private RoleService roleService;

  public RoleForm() {

    // Componentes
    txtRole = new TextField("Rol");
    txtRole.setRequiredIndicatorVisible(true);

    doCreateActionButtons();
    doCreateDialog();

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


    // Contenedor del formulario
    FormLayout formLayout = new FormLayout();
    formLayout.add(txtRole);

    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
    formLayout.addClassName("p-m");

    // Contenedor de eliminar
    HorizontalLayout leftButtonsLayout = new HorizontalLayout(btnDelete);
    leftButtonsLayout.addClassName("content-center");
    leftButtonsLayout.setWidthFull();

    // Contenedor de botones
    HorizontalLayout rightButtonsLayout = new HorizontalLayout(btnCancel, btnConfirm);
    rightButtonsLayout.addClassName("content-end");
    rightButtonsLayout.setSpacing(true);

    // Contenedor de botones
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.add(leftButtonsLayout, new Span(), rightButtonsLayout);
    buttonLayout.setWidthFull();
    buttonLayout.setPadding(true);
    buttonLayout.addClassNames("items-center");

    // Centrado para contenedor
    VerticalLayout formContainer = new VerticalLayout(formLayout, buttonLayout);
    buttonLayout.setPadding(true);
    formContainer.addClassName("content-start");
    formContainer.addClassName("items-center");
    add(header, formContainer);
  }


  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    // Formulario y validación con Binder
    binder.forField(txtRole)
        .asRequired("El rol es obligatorio")
        .bind(Role::getRole, Role::setRole);

    binder.readBean(data);

    txtRole.focus();
  }

  private void doCreateActionButtons() {
    // Botones
    btnCancel = new Button("Cancelar");
    btnConfirm = new Button("Aceptar");
    btnDelete = new Button(new Icon(VaadinIcon.TRASH));

    btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnConfirm.addFocusShortcut(Key.ENTER).listenOn(txtRole);

    btnDelete.addClassNames("bg-error", "text-error-contrast");
  }

  private void  doCreateDialog(){
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
      event.getSource().getUI().ifPresent(ui -> ui.navigate(RoleList.class));

    });

    btnCancel.addClickListener(event -> {
      event.getSource().getUI().ifPresent(ui -> ui.navigate(RoleList.class));
    });
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
        event.getSource().getUI().ifPresent(ui -> ui.navigate(RoleList.class));
      } catch (ValidationException e) {
        e.printStackTrace();
      }
    });

    btnDelete.addClickListener(event -> {
      dialog.open();
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
