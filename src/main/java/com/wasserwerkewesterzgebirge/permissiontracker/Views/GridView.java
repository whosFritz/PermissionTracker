package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wasserwerkewesterzgebirge.permissiontracker.Security.SecurityService;
import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import com.wasserwerkewesterzgebirge.permissiontracker.data.service.ZWW_Authorities_Service;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@PageTitle("Info Seite")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class GridView extends VerticalLayout {
    Grid<ZWW_Authority> berechtigungsTabelle = new Grid<>();

    public GridView(SecurityService securityService, ZWW_Authorities_Service zww_authorities_service) {
        List<ZWW_Authority> zwwAuthorityList = zww_authorities_service.findAllAuthorities();

        Collection<? extends GrantedAuthority> userAuthorities = securityService.getLoggedInUser().getUsers_authorities(); // your Collection of GrantedAuthorities

        List<ZWW_Authority> matchingAuthorities = new ArrayList<>();

        for (GrantedAuthority userAuthority : userAuthorities) {
            String authorityName = userAuthority.getAuthority();
            for (ZWW_Authority zwwAuthority : zwwAuthorityList) {
                if (zwwAuthority.getName().equals(authorityName)) {
                    matchingAuthorities.add(zwwAuthority);
                    break;
                }
            }
        }
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout passwordexpiredate = new HorizontalLayout();
        passwordexpiredate.add(new H4("Dein Passwort läuft am " + securityService.getLoggedInUser().getPasswordExpireDate()));
        add(passwordexpiredate);
        berechtigungsTabelle.setItems(matchingAuthorities);
        berechtigungsTabelle.addClassName("table-users-authorities");
        berechtigungsTabelle.addColumn(ZWW_Authority::getName).setHeader("Name").setSortable(true).setComparator(ZWW_Authority::getName);
        berechtigungsTabelle.addColumn(ZWW_Authority::getDescription).setHeader("Beschreibung").setSortable(true).setComparator(ZWW_Authority::getDescription);
        berechtigungsTabelle.getColumns().forEach(col -> col.setAutoWidth(true));

        add(berechtigungsTabelle);


//        StreamResource logoStream = new StreamResource("WW-Logo1.jpg", () -> getClass().getResourceAsStream("/static/Images/WW-Logo1.jpg"));
//        Image WW_start_logo = new Image(logoStream, "WW_start_logo");
//        WW_start_logo.setAlt("WW_start_logo");


    }


}
