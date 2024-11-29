package org.acme.pos.frontend.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "")
public class MainLayout extends AppLayout {
  private H1 viewTitle;

  public MainLayout() {
    setPrimarySection(Section.DRAWER);
    addNavbarContent();
    addDrawerContent();
  }

  private void addNavbarContent() {
    var toggle = new DrawerToggle();
    toggle.setAriaLabel("Menu toggle");
    toggle.setTooltipText("Menu toggle");

    viewTitle = new H1("MyApp");
    viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE,
        LumoUtility.Flex.GROW);

    var header = new Header(toggle, viewTitle);
    header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
        LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);

    addToNavbar(false, header);
  }

  private void addDrawerContent() {
    var appName = new Span("Dashboard");
    appName.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
        LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.SEMIBOLD,
        LumoUtility.Height.XLARGE, LumoUtility.Padding.Horizontal.MEDIUM
    );
    addToDrawer(appName, new Scroller(createSideNav()));
  }

  private SideNav createSideNav() {
    SideNav nav = new SideNav();

    nav.addItem(
        new SideNavItem("Customer List", CustomerList.class,
        VaadinIcon.USER.create()),

        new SideNavItem("Item List", ItemList.class,
            VaadinIcon.SORT.create()),

        new SideNavItem("Role List", RoleList.class,
            VaadinIcon.USERS.create()),

        new SideNavItem("User List", UserList.class,
            VaadinIcon.USER_CHECK.create())
    );

    return nav;
  }

  private String getCurrentPageTitle() {
    if (getContent() == null) {
      return "";
    } else if (getContent() instanceof HasDynamicTitle titleHolder) {
      return titleHolder.getPageTitle();
    } else {
      var title = getContent().getClass().getAnnotation(PageTitle.class);
      return title == null ? "" : title.value();
    }
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }
}