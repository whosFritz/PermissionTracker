package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.wasserwerkewesterzgebirge.permissiontracker.Security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private final Tabs menu;
    Logger logger = LoggerFactory.getLogger(MainLayout.class);

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        setPrimarySection(Section.DRAWER);
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));

    }

    private static Tab createTab(String text,
                                 Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();

        // Configure styling for the drawer
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Have a drawer header with an application logo
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Display the logo and the menu in the drawer
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{
                createTab("Deine Berechtigungen", GridView.class),
                createTab("Neue Berechtigungen anfragen", PermissionRequestView.class),
                createTab("About this WebApp", AboutView.class)
        };
    }

    private void createHeader() {
        H2 logo = new H2("Permission Tracker");
        logo.addClassNames("text-l", "m-m");
        Button logoutButton = new Button("Logout", event -> {
            logger.info("Benutzer wurde ausgeloggt: " + securityService.getLoggedInUser());
            securityService.logout();
        });
        logoutButton.setTooltipText("Hier kannst du dich ausloggen.");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                new Text("Eingeloggt als: "),
                new Text(securityService.getLoggedInUser().getGanzer_Name()),
                logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Select the tab corresponding to currently shown view
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }
    //    private void createDrawer() {
//
//        RouterLink listView = new RouterLink("", GridView.class); //TODO symbole einfügen;
//        RouterLink requestView = new RouterLink("", PermissionRequestView.class);
//        RouterLink aboutView = new RouterLink("About this WebApp", AboutView.class);
//        addToDrawer(new VerticalLayout(listView, requestView, aboutView));
//    }
}