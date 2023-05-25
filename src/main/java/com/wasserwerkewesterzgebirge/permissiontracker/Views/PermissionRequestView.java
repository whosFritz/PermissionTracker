package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.wasserwerkewesterzgebirge.permissiontracker.Mail.MailService;
import com.wasserwerkewesterzgebirge.permissiontracker.Security.SecurityService;
import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import com.wasserwerkewesterzgebirge.permissiontracker.data.service.PermissionRequestService;
import com.wasserwerkewesterzgebirge.permissiontracker.data.service.ZWW_Authorities_Service;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PageTitle("Berechtigung anfragen")
@Route(value = "request-permission", layout = MainLayout.class)
@PermitAll
public class PermissionRequestView extends VerticalLayout {
    Logger logger = LoggerFactory.getLogger(PermissionRequestService.class);
    StreamResource ArrowStream = new StreamResource("arrow-down-1-svgrepo-com.svg", () -> getClass().getResourceAsStream("/static/img/arrow-down-1-svgrepo-com.svg"));
    SecurityService securityService;
    ZWW_Authorities_Service zww_authorities_service;
    MailService mailService;
    PermissionRequestService permissionRequestService;
    String antragstellungsdatum;
    TextField filterText;
    TextArea sonstiges;
    Grid<ZWW_Authority> tableToChooseFrom;


    public PermissionRequestView(SecurityService securityService, ZWW_Authorities_Service zww_authorities_service, MailService mailService, PermissionRequestService permissionRequestService) {
        this.securityService = securityService;
        this.zww_authorities_service = zww_authorities_service;
        this.mailService = mailService;
        this.permissionRequestService = permissionRequestService;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm 'Uhr'");
        antragstellungsdatum = now.format(formatter);
        setSizeFull(); // Set the VerticalLayout to occupy the full available space


        // Linke Seite
        VerticalLayout groupSelection = new VerticalLayout();
        tableToChooseFrom = new Grid();
        tableToChooseFrom.setItems(zww_authorities_service.findAllAuthorities());
        tableToChooseFrom.addColumn(ZWW_Authority::getName).setHeader("Name").setSortable(true).setComparator(ZWW_Authority::getName).setKey("Name");
        tableToChooseFrom.addColumn(ZWW_Authority::getDescription).setHeader("Beschreibung").setSortable(true).setComparator(ZWW_Authority::getDescription);
        tableToChooseFrom.addColumn(ZWW_Authority::getCategory).setHeader("Kategorie").setSortable(true).setComparator(ZWW_Authority::getCategory);
        tableToChooseFrom.setSelectionMode(Grid.SelectionMode.MULTI);
        tableToChooseFrom.addItemClickListener(event -> event.getItem());
        tableToChooseFrom.getColumns().forEach(zwwAuthorityColumn -> zwwAuthorityColumn.setAutoWidth(true));
        tableToChooseFrom.setSizeFull();
        groupSelection.setSizeFull();
        groupSelection.add(getToolBar());
        groupSelection.add(tableToChooseFrom);

        // Rechte Seite
        VerticalLayout rightpanel = new VerticalLayout();
        sonstiges = new TextArea();
        sonstiges.setLabel("Anmerkungen");
        sonstiges.setWidthFull();
        sonstiges.setPlaceholder("Hier kannst du deinen Vorgesetzten oder dem System Anmerkungen hinterlassen.");

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Anfrage abschicken");
        dialog.setText("Wenn Sie bestätigen, wird Ihre Berechtigungsanfrage verschickt.");
        dialog.setCancelable(true);
        dialog.setCancelText("Abbruch");
        dialog.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
        dialog.setConfirmText("Bestätigen");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_SUCCESS.getVariantName());
        dialog.addConfirmListener(event -> {
            pressSendButton();
            tableToChooseFrom.deselectAll();
        });
        Button sendButton = new Button("Absenden", event -> {
            if (!tableToChooseFrom.getSelectedItems().isEmpty()) {
                dialog.open();
            } else {
                Notification.show("Bitte wähle die gewünschten Gruppen aus.");
            }
        });
        rightpanel.add(sonstiges, sendButton, createRequestFlow());

        // Links und Rechts
        HorizontalLayout links_rechts = new HorizontalLayout();
        groupSelection.setWidth(80, Unit.PERCENTAGE);
        rightpanel.setWidth(20, Unit.PERCENTAGE);
        rightpanel.setAlignItems(Alignment.CENTER);
        links_rechts.add(groupSelection, rightpanel);
        links_rechts.setSizeFull();

        add(links_rechts);
    }

    private Component getToolBar() {
        filterText = new TextField();
        filterText.setPlaceholder("Filter nach Namen...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(event -> updateList());
        return filterText;
    }

    private void updateList() {
        tableToChooseFrom.setItems(zww_authorities_service.searchForAuthorities(filterText.getValue()));
    }

    private VerticalLayout createRequestFlow() {
        VerticalLayout requestFlow = new VerticalLayout();
        Image downPointingArrow1 = new Image(ArrowStream, "Arrow");
        downPointingArrow1.setHeight("5%");
        requestFlow.setAlignItems(Alignment.CENTER);
        requestFlow.add(new H3(securityService.getLoggedInUser().getGanzer_Name() + " (Du)"));
        if (securityService.getLoggedInUser().getBoss1() != null) {
            Image downPointingArrow2 = new Image(ArrowStream, "Arrow");
            downPointingArrow2.setHeight("5%");
            requestFlow.add(downPointingArrow2);
            requestFlow.add(new H3(securityService.getLoggedInUser().getBoss1().getDisplayName()));
            if (securityService.getLoggedInUser().getBoss2() != null) {
                Image downPointingArrow3 = new Image(ArrowStream, "Arrow");
                downPointingArrow3.setHeight("5%");
                requestFlow.add(downPointingArrow3);
                requestFlow.add(new H3(securityService.getLoggedInUser().getBoss2().getDisplayName()));
                if (securityService.getLoggedInUser().getBoss3() != null) {
                    Image downPointingArrow4 = new Image(ArrowStream, "Arrow");
                    downPointingArrow4.setHeight("5%");
                    requestFlow.add(downPointingArrow4);
                    requestFlow.add(new H3(securityService.getLoggedInUser().getBoss3().getDisplayName()));
                    if (securityService.getLoggedInUser().getBoss4() != null) {
                        Image downPointingArrow5 = new Image(ArrowStream, "Arrow");
                        downPointingArrow5.setHeight("5%");
                        requestFlow.add(downPointingArrow5);
                        requestFlow.add(new H3(securityService.getLoggedInUser().getBoss4().getDisplayName()));
                    }
                }
            }
        }
        return requestFlow;
    }

    public void pressSendButton() {
        try {
            String yescode = RandomStringUtils.randomAlphanumeric(32);
            String nocode = RandomStringUtils.randomAlphanumeric(32);
            // Speichern dann laden und senden
            permissionRequestService.saveONE_PermissionRequest(new PermissionRequest
                    (securityService.getLoggedInUser().getGanzer_Name(),
                            securityService.getLoggedInUser().getMail() != null ? securityService.getLoggedInUser().getMail() : null, mailService.gruppenObjectToDatabaseString(tableToChooseFrom.getSelectedItems().stream().toList()), sonstiges.getValue(), antragstellungsdatum, "in Bearbeitung", 0, securityService.getLoggedInUser().getCount_bosses(),
                            securityService.getLoggedInUser().getBoss1() != null ? securityService.getLoggedInUser().getBoss1().getDisplayName() : null,
                            securityService.getLoggedInUser().getBoss1() != null ? securityService.getLoggedInUser().getBoss1().getEmail() : null,
                            securityService.getLoggedInUser().getBoss2() != null ? securityService.getLoggedInUser().getBoss2().getDisplayName() : null,
                            securityService.getLoggedInUser().getBoss2() != null ? securityService.getLoggedInUser().getBoss2().getEmail() : null,
                            securityService.getLoggedInUser().getBoss3() != null ? securityService.getLoggedInUser().getBoss3().getDisplayName() : null,
                            securityService.getLoggedInUser().getBoss3() != null ? securityService.getLoggedInUser().getBoss3().getEmail() : null,
                            securityService.getLoggedInUser().getBoss4() != null ? securityService.getLoggedInUser().getBoss4().getDisplayName() : null,
                            securityService.getLoggedInUser().getBoss4() != null ? securityService.getLoggedInUser().getBoss4().getEmail() : null,
                            yescode,
                            nocode));
            // Gespeichert
            PermissionRequest geladenePermissionRequest = permissionRequestService.findByYesCode(yescode);
            // Mail an mitarbeiter Quittung
            if (geladenePermissionRequest.getRequestersMail() != null)
                mailService.sendRequestQuittung(geladenePermissionRequest);
            // Mail an Vorgeetzten zum Bestätigen
            mailService.sendMailFirstBoss(geladenePermissionRequest);
            Notification success = new Notification("Eine Mail wurde an deinen Vorgesetzten zur Bestätigung geschickt.");
            success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            success.setDuration(10000);
            success.open();

        } catch (Exception e) {
            logger.error("Ein Fehler trat auf", e);
            Notification error = new Notification("Da lief wohl etwas schief \uD83D\uDE14   " + e.getStackTrace().toString());
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.setDuration(10000);
            error.open();
        }
    }
}
