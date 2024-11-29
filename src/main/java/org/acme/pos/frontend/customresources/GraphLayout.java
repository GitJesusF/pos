package org.acme.pos.frontend.customresources;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GraphLayout extends VerticalLayout {
  public GraphLayout(String txtHeaderTitle, String txtHeaderContent, String txtFooterContent, String imageUrl) {

    VerticalLayout layMain =new VerticalLayout();
    layMain.getStyle().set("background-color","white");
    layMain.addClassName("rounded-l");



    VerticalLayout layGraphHeader = new VerticalLayout();
    layGraphHeader.setPadding(false);
    layGraphHeader.setSpacing(false);

    Div divText1 = new Div(txtHeaderTitle);
    divText1.addClassNames("font-bold");
    Div divText2 = new Div(txtHeaderContent);
    divText2.addClassName("font-light");



    HorizontalLayout layGraphFooter = new HorizontalLayout();
    layGraphFooter.addClassNames("border-t","w-full","pt-m");
    layGraphFooter.setSpacing(false);

    Div divText3 = new Div("Schedule");
    divText3.addClassNames("font-light","me-xs");
    divText3.getStyle().set("font-style","italic");
    Div divText4 = new Div(txtFooterContent);
    divText4.addClassNames("font-light");


    Image image = new Image(imageUrl, "image description");


    layGraphHeader.add(divText1,divText2);
    layGraphFooter.add(divText3,divText4);

    layMain.add(image,layGraphHeader, layGraphFooter);

    this.add(layMain);
  }
}