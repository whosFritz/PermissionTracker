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
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route("approve")
@PageTitle("Bestätigt")
@AnonymousAllowed
public class ApproveView extends Composite implements BeforeEnterObserver {
    MailService mailService;
    PermissionRequestService permissionRequestService;
    Logger logger = LoggerFactory.getLogger(ApproveView.class);
    private VerticalLayout layout;

    ApproveView(MailService mailService, PermissionRequestService permissionRequestService) {
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
            PermissionRequest anfrage = permissionRequestService.findByYesCode(params.get("code").get(0));
            if (anfrage != null && anfrage.getStatus().equals("in Bearbeitung")) {
                String new_yescode = RandomStringUtils.randomAlphanumeric(32);
                String new_nocode = RandomStringUtils.randomAlphanumeric(32);
                anfrage.setApprovedLevels(anfrage.getApprovedLevels() + 1);
                anfrage.setYesCode(new_yescode);
                anfrage.setNoCode(new_nocode);
                StreamResource logoStream = new StreamResource("happy-emoji-up-down.gif", () -> getClass().getResourceAsStream("/static/gifs/happy-emoji-up-down.gif"));
                if (anfrage.getApprovedLevels() < anfrage.getToBeApprovedLevels()) {
                    if (!(anfrage.getRequestersMail() == null))
                        mailService.sendApproveEmployeeInfo(anfrage, decissionDate);
                    mailService.sendMailToNextBoss(anfrage, decissionDate);
                    mailService.sendApproveBossInfo(anfrage, decissionDate);
                    Image emojiGif = new Image(logoStream, "happy-emoji-up-down.gif");
                    layout.add(emojiGif, new H3("Erfolgreich bestätigt."));
                } else {
                    if (!(anfrage.getRequestersMail() == null))
                        mailService.sendFinalMailToEmployee(anfrage, decissionDate);
                    mailService.sendMailToEDV(anfrage, decissionDate);
                    mailService.sendFinalMailBossApproveInfo(anfrage, decissionDate);
                    Image emojiGif = new Image(logoStream, "happy-emoji-up-down.gif");
                    layout.add(emojiGif, new H3("Eine Mail wird jetzt für die EDV generiert"));
                }
            } else {
                StreamResource logoStream = new StreamResource("exploding-head-emoji.gif", () -> getClass().getResourceAsStream("/static/gifs/exploding-head-emoji.gif"));
                Image emojiGif = new Image(logoStream, "exploding-head-emoji.gif");
                layout.add(emojiGif, new H3("Anfrage wurde schon bearbeitet oder existiert nicht"));
                logger.info("Kein Objekt gefunden zum Bestätigen mit ID: " + anfrage.getId());
            }
        } catch (Exception e) {
            StreamResource logoStream = new StreamResource("emoji-sad-animated.gif", () -> getClass().getResourceAsStream("/static/gifs/emoji-sad-animated.gif"));
            Image emojiGif = new Image(logoStream, "emoji-sad-animated.gif");
            layout.add(emojiGif, new H3("Es trat ein Fehler auf"));
            logger.error("Fehler beim Bestätigen", e);
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
