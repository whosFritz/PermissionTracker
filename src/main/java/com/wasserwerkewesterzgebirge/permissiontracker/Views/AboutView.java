package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This class is used to display the about page.
 * It is used to display information about the application.
 * It is also used to display the contact information of the developer.
 */
@PageTitle("About Page")
@Route(value = "about-page", layout = MainLayout.class)
@PermitAll
public class AboutView extends VerticalLayout {
    public AboutView() {
        add(
                new H1("Permission Tracker"),
                new H4("Diese Webapplikation wurde im Zuge einer Projektarbeit von Fritz Schubert entwickelt. Bei Fragen gerne an ihn wenden"),
                new H5("Email: Fritz.Schubert@wasserwerke-westerzgebirge.de"),
                new H5("Telefon: 03774 144-197")
        );

    }
}
