package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("customer")
public class CustomerForm extends VerticalLayout implements HasUrlParameter<Integer> {

  private Customer data;
  private final Binder<Customer> binder = new Binder<>(Customer.class);

  private TextField txtFirstName;
  private TextField txtLastName;
  private EmailField txtEmail;
  private TextField txtPhone;
  private Button btnDelete;
  private Button btnCancel;
  private Button btnConfirm;

  @Autowired
  private CustomerService customerService;

  //Constructor
  public CustomerForm() {

    HorizontalLayout header = doCreateHeader();
    FormLayout form = doCreateForm();
    HorizontalLayout layAction = doCreateActionButtons();

    // Centrado para contenedor
    this.add(header, form, layAction);
    this.addClassName("content-start");
    this.addClassName("items-center");
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    // Formulario y validación con Binder
    binder.forField(txtFirstName)
        .asRequired("El nombre es obligatorio")
        .bind(Customer::getFirstName, Customer::setFirstName);

    binder.forField(txtLastName)
        .asRequired("El apellido es obligatorio")
        .bind(Customer::getLastName, Customer::setLastName);

    binder.forField(txtEmail)
        .asRequired("El correo electrónico es obligatorio")
        .withValidator(email -> email.contains("@"), "Correo electrónico inválido")
        .bind(Customer::getEmail, Customer::setEmail);

    binder.forField(txtPhone)
        .asRequired("El número de teléfono es obligatorio")
        .withValidator(phone -> phone.matches("^[(]?[0-9]{3}[)]?[-]?[0-9]{3}[-]?[0-9]{4}$"), "Formato de teléfono inválido")
        .bind(Customer::getPhone, Customer::setPhone);

    binder.readBean(data);

    txtFirstName.focus();
  }

  private HorizontalLayout doCreateHeader() {
    // Encabezado
    HorizontalLayout header = new HorizontalLayout();
    header.setId("header");
    header.getThemeList().set("dark", true);
    header.setWidthFull();
    header.setSpacing(false);
    header.addClassName("items-center");
    header.add(new DrawerToggle());
    H1 viewTitle = new H1("Sección de clientes");
    header.add(viewTitle);
    return header;
  }

  private FormLayout doCreateForm(){

    // Componentes
    txtFirstName = new TextField("Nombre");
    txtLastName = new TextField("Apellido");
    txtEmail = new EmailField("Correo electrónico");
    txtPhone = new TextField("Número de teléfono", "(123)-456-7890");

    txtFirstName.setRequiredIndicatorVisible(true);

    txtLastName.setRequiredIndicatorVisible(true);
    txtLastName.addFocusShortcut(Key.ENTER).listenOn(txtFirstName);

    txtEmail.setErrorMessage("Ingrese una dirección de correo electrónico válida");
    txtEmail.setRequiredIndicatorVisible(true);
    txtEmail.addFocusShortcut(Key.ENTER).listenOn(txtLastName);


    txtPhone.setPattern("^[\\(]?[0-9]{3}[\\)]?[\\-]?[0-9]{3}[\\-]?[0-9]{4}$");
    txtPhone.setAllowedCharPattern("[0-9]");
    txtPhone.setMaxLength(12);
    txtPhone.setErrorMessage("Ingrese un número de teléfono válido");
    txtPhone.setRequiredIndicatorVisible(true);
    txtPhone.addFocusShortcut(Key.ENTER).listenOn(txtEmail);

    // Diseño de formulario
    FormLayout formLayout = new FormLayout();
    formLayout.setColspan(txtEmail, 2);
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
    formLayout.addClassName("p-m");

    formLayout.add(txtFirstName, txtLastName, txtEmail, txtPhone);
    return formLayout;
  }

  private HorizontalLayout doCreateActionButtons(){
    // Botones
    btnCancel = new Button("Cancelar");
    btnCancel.addClickListener(event ->
        event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class)));

    btnConfirm = new Button("Aceptar");
    btnConfirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btnConfirm.addFocusShortcut(Key.ENTER).listenOn(txtPhone);
    btnConfirm.addClickListener(event -> {
      // This makes all current validation errors visible.
      BinderValidationStatus<Customer> status = binder.validate();
      if (status.hasErrors()) {
        //Notification.show(status.getValidationErrors().toString(), 5000, Notification.Position.MIDDLE);
        return;
      }

      try {
        binder.writeBean(data);
        customerService.saveCustomer(data);
        event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class));
      } catch (ValidationException e) {
        e.printStackTrace();
      }
    });

    btnDelete = new Button(new Icon(VaadinIcon.TRASH));
    btnDelete.addClassNames("bg-error", "text-error-contrast", "mr-auto");
    btnDelete.addClickListener(event -> openConfirmDeleteDialog());

    //Diseño del contenedor de botones
    HorizontalLayout layAction = new HorizontalLayout(btnDelete, btnCancel, btnConfirm);
    layAction.setWidthFull();
    layAction.addClassName("justify-end");
    layAction.setPadding(true);

    return layAction;
  }

  private void openConfirmDeleteDialog(){
    // Dialogo de eliminación
    ConfirmDialog dialog = new ConfirmDialog();
    dialog.setHeader("¿Está muy seguro de eliminar este cliente?");
    dialog.setText("Una vez eliminado, no se podrá recuperar la información.");
    dialog.setCancelText("No");
    dialog.setCancelable(true);
    dialog.setConfirmText("Sí");
    dialog.setConfirmButtonTheme("error primary");

    dialog.addConfirmListener(event -> {
      customerService.deleteCustomerById(data.getId());
      event.getSource().getUI().ifPresent(ui -> ui.navigate(CustomerList.class));
    });
    dialog.open();
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
    if (id != null) {
      data = customerService.getCustomerById(id).orElse(new Customer());
      btnDelete.setEnabled(true);
      btnDelete.setVisible(true);
    } else {
      data = new Customer();
      btnDelete.setEnabled(false);
      btnDelete.setVisible(false);
    }
  }
}
