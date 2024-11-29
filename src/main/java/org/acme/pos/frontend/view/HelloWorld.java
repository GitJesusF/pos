package org.acme.pos.frontend.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.acme.pos.backend.entity.Customer;
import org.acme.pos.frontend.customresources.GraphLayout;
import org.acme.pos.frontend.customresources.InfoLayout;

@Route("hello")
public class HelloWorld extends VerticalLayout {

  public HelloWorld() {
    this.setSizeFull();
    this.addClassName("bg-contrast-20");

    HorizontalLayout layHeader = doCreateHeader();
    HorizontalLayout layNumber = doCreateNumberLayout();
    HorizontalLayout layGraph = doCreateGraphLayout();
    HorizontalLayout layLast = doCreateLastLayout();

    this.add(layHeader, layNumber, layGraph, layLast);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
  }

  private HorizontalLayout doCreateHeader() {
    Div divText = new Div();
    divText.setText("Dashboard");

    TextField txtSearch = new TextField();
    txtSearch.setPlaceholder("Type here…");
    txtSearch.addClassName("ml-auto");

    Button btnSignIn= new Button(VaadinIcon.USER.create());
    btnSignIn.setText("Sign In");
    btnSignIn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

    Button btnPageConfig = new Button(VaadinIcon.COG.create());
    btnPageConfig.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

    Button btnNotifications = new Button(VaadinIcon.BELL_O.create());
    btnNotifications.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);


    HorizontalLayout layHeader = new HorizontalLayout();
    layHeader.setWidthFull();
    layHeader.addClassNames("items-center","justify-end");
    layHeader.add(divText, txtSearch, btnSignIn,btnPageConfig,btnNotifications);

    return layHeader;
  }

  private HorizontalLayout doCreateNumberLayout() {

    InfoLayout layFirst = new InfoLayout(
        "Today's Money",
        "$53k",
        "images/img1.png",
        "+55% than last week"
    );

    InfoLayout laySecond = new InfoLayout(
        "Today's User",
        "2,300",
        "images/img2.png",
        "+3% than last month"
    );

    InfoLayout layThird = new InfoLayout(
        "New Clients",
        "3,462",
        "images/img3.png",
        "-2% than yesterday"
    );

    InfoLayout layFourth = new InfoLayout(
        "Sales",
        "$103,430",
        "images/img4.png",
        "+5% than yesterday"
    );

    HorizontalLayout layNumber = new HorizontalLayout();
    layNumber.setWidthFull();
    layNumber.add(layFirst, laySecond, layThird, layFourth);

    return layNumber;
  }

  private HorizontalLayout doCreateGraphLayout() {

    GraphLayout layGraph1 = new GraphLayout(
        "Website Views",
        "Last Compaign",
        "campaign sent 2 days ago",
        "images/grafico1.png"
    );

    GraphLayout layGraph2 = new GraphLayout(
        "Daily Sales",
        "(+15%) increase in today sales.",
        "updated 4 min ago",
        "images/grafico2.png"
    );

    GraphLayout layGraph3 = new GraphLayout(
        "Complated Tasks",
        "Last Campaign Performance",
        "just uploaded",
        "images/grafico3.png"
    );

    HorizontalLayout layGraph = new HorizontalLayout();
    layGraph.setWidthFull();
    layGraph.add(layGraph1,layGraph2, layGraph3);
    return layGraph;
  }

  private HorizontalLayout doCreateLastLayout() {

    Div divGridText1 = new Div("Grid");
    divGridText1.addClassName("font-bold");

    HorizontalLayout gridHeader = new HorizontalLayout();
    gridHeader.setWidthFull();

    Div divGridText2 = new Div();
    divGridText2.getElement().setText("30 records this month");
    divGridText2.getElement().setProperty("innerHTML", "<b>30 records</b> this month");
    divGridText2.addClassNames("font-light");

    Div divRecordCheck = new Div(VaadinIcon.CHECK.create());
    divRecordCheck.getElement().getStyle().set("color", "#005eda");

    Button btnDotsV= new Button(VaadinIcon.ELLIPSIS_DOTS_V.create());
    btnDotsV.addClassNames("ml-auto","bg-transparent");
    gridHeader.addClassNames("items-center");
    gridHeader.setHeight("5%");
    gridHeader.add(divRecordCheck,divGridText2,btnDotsV);

    Grid<Customer> grid = new Grid<>(Customer.class, false);
    grid.setSizeFull();

    grid.addColumn(Customer::getLastName).setKey("lastName").setHeader("Apellido").setSortable(true).setFlexGrow(1);
    grid.addColumn(Customer::getFirstName).setKey("firstName").setHeader("Nombre").setSortable(true).setFlexGrow(1);
    grid.addColumn(Customer::getEmail).setHeader("Email").setFlexGrow(1);
    grid.addColumn(Customer::getPhone).setHeader("Teléfono ").setFlexGrow(0).setAutoWidth(true);

    VerticalLayout layGrid = new VerticalLayout();
    layGrid.setSizeFull();
    layGrid.addClassName("rounded-l");
    layGrid.getStyle().set("background-color","white");
    layGrid.add(divGridText1,gridHeader,grid);



    Div divText1 = new Div("Order overview");
    divText1.addClassNames("font-bold");

    Div divArrowUp = new Div(VaadinIcon.ARROW_UP.create());
    divArrowUp.getElement().getStyle().set("color", "green"); // Set the color to red
    divArrowUp.addClassName("success");
    Div divText2 = new Div();
    divText2.getElement().setText("24% this month");
    divText2.getElement().setProperty("innerHTML","<b>24%</b> this month");
    divText2.addClassNames("font-light");

    VerticalLayout layOverview = new VerticalLayout();
    layOverview.setWidth("33%");
    layOverview.addClassName("rounded-l");
    layOverview.getStyle().set("background-color","white");

    Div divText3 = new Div("$2400, Design changes");
    divText3.addClassName("font-bold");
    Div divText4 = new Div("11 NOV 7:20 PM");
    divText4.addClassNames("font-light","mb-m");

    Div divText5 = new Div("New order #1832412");
    divText5.addClassName("font-bold");
    Div divText6 = new Div("19 NOV 9:11 AM");
    divText6.addClassNames("font-light","mb-m");

    Div divText7 = new Div("Server payments for April");
    divText7.addClassName("font-bold");
    Div divText8 = new Div("09 NOV 6:25 AM");
    divText8.addClassNames("font-light","mb-m");


    Div divText9 = new Div("New card added for order #4395133");
    divText9.addClassName("font-bold");
    Div divText10 = new Div("20 NOV 3:49 AM");
    divText10.addClassNames("font-light","mb-m");

    Div divText11 = new Div("Unlock packages for development");
    divText11.addClassName("font-bold");
    Div divText12 = new Div("18 NOV 4:44 AM");
    divText12.addClassNames("font-light","mb-m");

    Div divText13 = new Div("New order #9583120");
    divText13.addClassName("font-bold");
    Div divText14 = new Div("17 DEC");
    divText14.addClassNames("font-light","mb-m");

    VerticalLayout layLine = new VerticalLayout();
    layLine.addClassName("border-l");
    layLine.setWidth("0px");
    layLine.setPadding(false);
    layLine.setSpacing(false);
    layLine.setMargin(false);

    VerticalLayout layText = new VerticalLayout();
    layText.setSpacing(false);
    layText.setWidthFull();
    layText.add(divText3,divText4,divText5,divText6,divText7,divText8,divText9,divText10,divText11,divText12,divText13,divText14);


    HorizontalLayout layHeaderOverview = new HorizontalLayout();
    layHeaderOverview.setWidthFull();
    layHeaderOverview.setSpacing(false);
    layHeaderOverview.add(divArrowUp,divText2);


    HorizontalLayout layOverviewContent = new HorizontalLayout();
    layOverviewContent.setWidthFull();
    layOverviewContent.add(layLine,layText);

    layOverview.add(divText1,layHeaderOverview, layOverviewContent);

    HorizontalLayout layLast = new HorizontalLayout();
    layLast.setWidthFull();
    layLast.addClassName("bg-transparent");

    layLast.add(layGrid,layOverview);
    return layLast;
  }
}