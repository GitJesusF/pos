package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.entity.User;
import org.acme.pos.backend.service.RoleService;
import org.acme.pos.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user")
public class UserForm extends VerticalLayout implements HasUrlParameter<Integer> {

  private User data;
  private final Binder<User> binder = new Binder<>(User.class);

  private TextField txtFirstName;
  private TextField txtLastName;
  private TextField txtUsername;
  private ComboBox<Role> cbxRoles;
  private EmailField txtEmail;
  private PasswordField txtPassword;

  private Button btnDelete;
  private Button btnCancel;
  private Button btnConfirm;

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  public UserForm() {

    HorizontalLayout header = doCreateHeader();
    FormLayout form = doCreateForm();
    HorizontalLayout layAction = doCreateActionButtons();


    this.add(header, form, layAction);
    this.addClassName("content-start");
    this.addClassName("items-center");
  }

  //Este metodo se ejecuta despues de construir la clase
  @PostConstruct
  private void init() {
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);


    // Formulario y validación con Binder
    binder.forField(txtFirstName)
        .asRequired("El nombre es obligatorio")
        .bind(User::getFirstName, User::setFirstName);

    binder.forField(txtLastName)
        .asRequired("El apellido es obligatorio")
        .bind(User::getLastName, User::setLastName);

    binder.forField(txtUsername)
        .asRequired("El usario es obligatorio")
        .bind(User::getUsername, User::setUsername);

    binder.forField(cbxRoles)
        .asRequired("El rol es obligatorio")
        .bind(User::getRole, User::setRole);

    binder.forField(txtEmail)
        .asRequired("El correo electrónico es obligatorio")
        .withValidator(email -> email.contains("@"), "Correo electrónico inválido")
        .bind(User::getEmail, User::setEmail);

    binder.forField(txtPassword)
        .asRequired("La contrseña es obligatoria")
        .bind(User::getPassword, User::setPassword);

    // load combobox items
    //cbxRoles.setItems(roleService.getAllRoles());
    cbxRoles.setItemLabelGenerator(Role::getName);

    binder.readBean(data);

    // check for read only/disable components
    // cbxRoles.setEnables(userLogged.isAdministrator());

    txtFirstName.focus();
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
    H1 viewTitle = new H1("Sección de usuarios");
    header.add(viewTitle);
    return header;
  }

  private FormLayout doCreateForm() {
    // Componentes
    txtFirstName = new TextField("Nombre");
    txtLastName = new TextField("Apellido");
    txtUsername = new TextField("Usuario");
    cbxRoles = new ComboBox<>("Rol");
    txtEmail = new EmailField("Correo electrónico");
    txtPassword = new PasswordField("Contraseña");

    txtFirstName.setRequiredIndicatorVisible(true);

    txtLastName.setRequiredIndicatorVisible(true);
    txtLastName.addFocusShortcut(Key.ENTER).listenOn(txtFirstName);

    txtUsername.setErrorMessage("Ingrese un usario válido");
    txtUsername.setRequiredIndicatorVisible(true);
    txtUsername.addFocusShortcut(Key.ENTER).listenOn(txtLastName);

    cbxRoles.setErrorMessage("Selecciona un rol");
    cbxRoles.setRequiredIndicatorVisible(true);
    cbxRoles.addFocusShortcut(Key.ENTER).listenOn(txtUsername);

    txtEmail.setErrorMessage("Ingrese una dirección de correo electrónico válida");
    txtEmail.setRequiredIndicatorVisible(true);
    txtEmail.addFocusShortcut(Key.ENTER).listenOn(cbxRoles);

    txtPassword.setErrorMessage("Ingrese una contraseña válida");
    txtPassword.setRequiredIndicatorVisible(true);
    txtPassword.addFocusShortcut(Key.ENTER).listenOn(txtEmail);

    // Contenedor del formulario
    FormLayout formLayout = new FormLayout();
    formLayout.setColspan(txtPassword, 2);
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
    formLayout.addClassName("p-m");

    formLayout.add(txtFirstName, txtLastName, txtUsername, cbxRoles, txtEmail, txtPassword);
    return formLayout;
  }

  private HorizontalLayout doCreateActionButtons() {
    // Botones

    btnDelete = new Button(new Icon(VaadinIcon.TRASH));
    btnDelete.addClassNames("bg-error", "text-error-contrast");
    btnDelete.addClickListener(event -> openConfirmDeleteDialog());

    btnCancel = new Button("Cancelar");

    btnConfirm = new Button("Aceptar");
    btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnConfirm.addFocusShortcut(Key.ENTER).listenOn(txtPassword);
    btnConfirm.addClickListener(event -> {
      // This makes all current validation errors visible.
      BinderValidationStatus<User> status = binder.validate();
      if (status.hasErrors()) {
        //Notification.show(status.getValidationErrors().toString(), 5000, Notification.Position.MIDDLE);
        return;
      }

      try {
        binder.writeBean(data);
        userService.saveUser(data);
        event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class));
      } catch (ValidationException e) {
        e.printStackTrace();
      }
    });

    // Contenedor de eliminar
    HorizontalLayout layAction = new HorizontalLayout(btnDelete, btnCancel, btnConfirm);
    layAction.setWidthFull();
    layAction.addClassName("justify-end");
    layAction.setPadding(true);

    return layAction;
  }

  private void openConfirmDeleteDialog(){
    // Dialogo de eliminación
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setHeader("¿Está muy seguro de eliminar este usuario?");
    dialog.setText("Una vez eliminado, no se podrá recuperar la información.");
    dialog.setCancelText("No");
    dialog.setCancelable(true);
    dialog.setConfirmText("Sí");
    dialog.setConfirmButtonTheme("error primary");

    dialog.addConfirmListener(event -> {
      userService.deleteUserById(data.getId());
      event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class));
    });
    dialog.open();
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
    if (id != null) {
      data = userService.getUserById(id).orElse(new User());
      btnDelete.setEnabled(true);
      btnDelete.setVisible(true);
    } else {
      data = new User();
      btnDelete.setEnabled(false);
      btnDelete.setVisible(false);
    }
  }

}
