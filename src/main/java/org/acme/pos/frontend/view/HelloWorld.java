package org.acme.pos.frontend.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("hello")
public class HelloWorld extends Div {

  public HelloWorld() {
    TextField txtName = new TextField("Name");


    Button btnSayHello = new Button("Say hello");
    btnSayHello.addClickListener(event -> Notification.show("Hello " + txtName.getValue()));

    this.add(txtName, btnSayHello);

  }
}
