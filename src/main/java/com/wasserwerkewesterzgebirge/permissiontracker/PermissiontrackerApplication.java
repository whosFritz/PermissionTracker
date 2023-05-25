package com.wasserwerkewesterzgebirge.permissiontracker;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PWA(name = "Permission Tracker", shortName = "PermTrack")
@Theme("permission-tracker")
public class PermissiontrackerApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(PermissiontrackerApplication.class, args);
    }

}
