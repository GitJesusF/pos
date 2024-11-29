package org.acme.pos.frontend.customresources;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

public class InfoLayout extends VerticalLayout {

  public InfoLayout(String text1, String text2, String imageUrl, String footerText) {

    VerticalLayout layMain = new VerticalLayout();
    layMain.getStyle().set("background-color","white");
    layMain.addClassName("rounded-l");


    HorizontalLayout layHeader = new HorizontalLayout();
    layHeader.setWidthFull();
    Image image = new Image(imageUrl, "image description");
    image.setWidth("82px");
    image.setHeight("82px");



    VerticalLayout layText = new VerticalLayout();
    layText.setWidthFull();
    layText.setPadding(false);
    layText.setSpacing(false);
    Div divText1 = new Div(text1);
    divText1.addClassNames("w-full", "font-light", "text-right", "text-xs");
    Div divText2 = new Div(text2);
    divText2.addClassNames("w-full", "font-bold", "text-right", "text-xl");


    HorizontalLayout layFooter = new HorizontalLayout();
    layFooter.setWidthFull();
    layFooter.addClassNames("border-t", "py-s");
    Div divText3 = new Div(footerText);
    divText3.addClassNames("text-xs");


    layHeader.add(image, layText);
    layText.add(divText1, divText2);
    layFooter.add(divText3);

    layMain.add(layHeader, layFooter);


    add(layMain);
  }
}