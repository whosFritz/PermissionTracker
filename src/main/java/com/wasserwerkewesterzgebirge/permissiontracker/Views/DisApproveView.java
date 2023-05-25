package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.wasserwerkewesterzgebirge.permissiontracker.Mail.MailService;
import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.data.service.PermissionRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route("disapprove")
@PageTitle("Nicht Bestätigt")
@AnonymousAllowed
public class DisApproveView extends Composite implements BeforeEnterObserver {
    MailService mailService;
    PermissionRequestService permissionRequestService;
    Logger logger = LoggerFactory.getLogger(ApproveView.class);
    private VerticalLayout layout;

    DisApproveView(MailService mailService, PermissionRequestService permissionRequestService) {
        this.mailService = mailService;
        this.permissionRequestService = permissionRequestService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm 'Uhr'");
        String decissionDate = now.format(formatter);

        Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();

        try {
            PermissionRequest anfrage = permissionRequestService.findByNoCode(params.get("code").get(0));
            if (anfrage != null && anfrage.getStatus().equals("in Bearbeitung")) { // existiert
                if (!(anfrage.getRequestersMail() == null)) {
                    mailService.sendCancelInfoEmployee(anfrage, decissionDate);
                }
                mailService.sendCancelInfoBoss(anfrage, decissionDate);
                anfrage.setStatus("Abgelehnt");
                permissionRequestService.saveONE_PermissionRequest(anfrage);

                StreamResource logoStream = new StreamResource("trash-can-animation.gif", () -> getClass().getResourceAsStream("/static/gifs/trash-can-animation.gif"));
                Image emojiGif = new Image(logoStream, "trash-can-animation.gif");
                layout.add(emojiGif, new H3("Die Anfrage wurde hiermit gelöscht"));
            } else { // wenn Eintrag nicht existiert
                StreamResource logoStream = new StreamResource("exploding-head-emoji.gif", () -> getClass().getResourceAsStream("/static/gifs/exploding-head-emoji.gif"));
                Image emojiGif = new Image(logoStream, "exploding-head-emoji.gif");
                layout.add(emojiGif, new H3("Anfrage wurde schon bearbeitet oder existiert nicht"));
                logger.info("Kein Objekt gefunden zum Ablehnen mit ID: " + anfrage.getId());
            }
        } catch (Exception e) {
            StreamResource logoStream = new StreamResource("emoji-sad-animated.gif", () -> getClass().getResourceAsStream("/static/gifs/emoji-sad-animated.gif"));
            Image emojiGif = new Image(logoStream, "emoji-sad-animated.gif");
            layout.add(emojiGif, new H3("Es trat ein Fehler auf"));
            logger.error("Fehler beim Nicht-Bestätigen", e);
        }
    }

    @Override
    protected Component initContent() {
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignSelf(FlexComponent.Alignment.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }
}
