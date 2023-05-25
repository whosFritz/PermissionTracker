package com.wasserwerkewesterzgebirge.permissiontracker.Mail;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.PermissionRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import com.wasserwerkewesterzgebirge.permissiontracker.data.service.PermissionRequestService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MailService {
    private final Message mimeMessage;
    private final PermissionRequestService permissionRequestService;
    Logger logger = LoggerFactory.getLogger(MailService.class);
    @Value("${mail.edv.mail}")
    private String EDV_MAIL;
    @Value("${mail.gf.name}")
    private String GF_NAME;
    @Value("${mail.gf.mail}")
    private String GF_MAIL;
    @Value("${mail.sender.address}")
    private String SENDER_MAIL_ADRESSE;


    //mail.sender.adress defined in application.properties
    public MailService(@Qualifier("messageContent") Message mimeMessage, PermissionRequestService permissionRequestService) {
        this.mimeMessage = mimeMessage;
        this.permissionRequestService = permissionRequestService;
    }

    public void sendRequestQuittung(PermissionRequest anfrage) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anfrage.getRequestersMail()));
        mimeMessage.setSubject("Quittung deiner Berechtigungsanfrage");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_Quittung(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                anfrage.getBoss1DisplayName(),
                anfrage.getDatumRequest(),
                anfrage.getRequestedPermissions(),
                anfrage.getSonstiges()
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email Quittung versendet: " + anfrage);
    }

    public void sendMailFirstBoss(PermissionRequest anfrage) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anfrage.getBoss1Mail()));
        mimeMessage.setSubject("Berechtigungsanfrage von " + anfrage.getRequestingPerson());

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_BossToApproveRequest(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                anfrage.getBoss1DisplayName(),
                anfrage.getDatumRequest(),
                anfrage.getRequestedPermissions(),
                anfrage.getSonstiges(),
                anfrage.getYesCode(),
                anfrage.getNoCode()
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an ersten Boss: " + anfrage);
    }


    public void sendMailToNextBoss(PermissionRequest anfrage, String datum) throws MessagingException {
        // new Codes
        anfrage.setYesCode(RandomStringUtils.randomAlphanumeric(32));
        anfrage.setNoCode(RandomStringUtils.randomAlphanumeric(32));

        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(whichBossToSendNext_Mail(anfrage)));
        mimeMessage.setSubject("Berechtigungsanfrage von " + anfrage.getRequestingPerson());
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_BossToApproveRequest(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                whichBossToSendNext_Name(anfrage),
                datum,
                anfrage.getRequestedPermissions(),
                anfrage.getSonstiges(),
                anfrage.getYesCode(),
                anfrage.getNoCode()
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);

        if (whichDateToSet(anfrage) == 1)
            anfrage.setDatumBoss1(datum);
        if (whichDateToSet(anfrage) == 2)
            anfrage.setDatumBoss2(datum);
        if (whichDateToSet(anfrage) == 3)
            anfrage.setDatumBoss3(datum);
        if (whichDateToSet(anfrage) == 4)
            anfrage.setDatumBoss4(datum);
        permissionRequestService.saveONE_PermissionRequest(anfrage);
        logger.info("Email an nächsten Vorgesetzten geschickt: " + anfrage);
    }

    public void sendApproveBossInfo(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(whichBossApproved_mail(anfrage)));
        mimeMessage.setSubject("Berechtigungsanfrage von " + anfrage.getRequestingPerson() + " bestätigt");
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedContent = createDynamicMailContent_ApproveInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                whichBossApproved_name(anfrage),
                whichBossToSendNext_Name(anfrage),
                datum,
                StaticEmailText.BOSS_APPROVE_INFO
        );
        mimeBodyPart.setContent(renderedContent, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Zwischenvorgesetzter, dass er bestätigt hat: " + anfrage);
    }


    public void sendApproveEmployeeInfo(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anfrage.getRequestersMail()));
        mimeMessage.setSubject("Berechtigungsanfrage bestätigt von: " + whichBossApproved_name(anfrage));
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedContent = createDynamicMailContent_ApproveInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                whichBossApproved_name(anfrage),
                whichBossToSendNext_Name(anfrage),
                datum,
                StaticEmailText.EMPLOYEE_APPROVE_INFO
        );
        mimeBodyPart.setContent(renderedContent, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Mitarbeiter, dass sein Zwischenvorgesetzter bestätigt hat: " + anfrage);
    }

    public void sendMailToEDV(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EDV_MAIL));
        mimeMessage.setSubject("Zugriffsberechtigung dürfen geändert werden");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_EDV(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                datum,
                anfrage.getSonstiges(),
                anfrage.getRequestedPermissions());
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Mitarbeiter der EDV, dass Änderungen vorgenommen wreden können: " + anfrage);
    }

    public void sendFinalMailToEmployee(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anfrage.getRequestersMail()));
        mimeMessage.setSubject("Berechtigungsanfrage Ergebnis");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_FinalApproveInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                datum,
                StaticEmailText.FINAL_EMPLOYEE_APPROVE
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Mitarbeiter, dass komplett genehmigt wurde: " + anfrage);
    }

    public void sendFinalMailBossApproveInfo(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(GF_MAIL));
        mimeMessage.setSubject("Berechtigungsanfrage Ergebnis");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_FinalApproveInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                datum,
                StaticEmailText.FINAL_BOSS_APPROVE
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);

        if (whichDateToSet(anfrage) == 1)
            anfrage.setDatumBoss1(datum);
        if (whichDateToSet(anfrage) == 2)
            anfrage.setDatumBoss2(datum);
        if (whichDateToSet(anfrage) == 3)
            anfrage.setDatumBoss3(datum);
        if (whichDateToSet(anfrage) == 4)
            anfrage.setDatumBoss4(datum);
        anfrage.setStatus("Genehmigt");
        logger.info("Email an GF, dass er komplett genehmigt hat: " + anfrage);
        permissionRequestService.saveONE_PermissionRequest(anfrage);
    }


    public void sendCancelInfoBoss(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(whichBossCanceled_Mail(anfrage)));
        mimeMessage.setSubject("Berechtigungsanfrage von " + anfrage.getRequestingPerson() + " abgelehnt");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_CancelInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                whichBossCanceled_Name(anfrage),
                datum,
                StaticEmailText.BOSS_CANCEL_INFO
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Mitarbeiter, dass seine Anfrage abgelehnt wurde: " + anfrage);
    }

    public void sendCancelInfoEmployee(PermissionRequest anfrage, String datum) throws MessagingException {
        mimeMessage.setFrom(new InternetAddress(SENDER_MAIL_ADRESSE));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anfrage.getRequestersMail()));
        mimeMessage.setSubject("Berechtigungsanfrage abgelehnt von " + whichBossCanceled_Name(anfrage));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String renderedBody = createDynamicMailContent_CancelInfo(
                anfrage.getId(),
                anfrage.getRequestingPerson(),
                whichBossCanceled_Name(anfrage),
                datum,
                StaticEmailText.EMPLOYEE_CANCEL_INFO
        );
        mimeBodyPart.setContent(renderedBody, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
        logger.info("Email an Vorgesetzten, dass er die Anfrage abgelehnt hat: " + anfrage);
    }

    public String createDynamicMailContent_CancelInfo(Long id, String antragsteller, String ablehner, String datum, String msg) {
        msg = msg.replaceAll("%Antragsteller", antragsteller);
        msg = msg.replaceAll("%Vorgesetzter", ablehner); // Vorgesetzter
        msg = msg.replaceAll("%Datum", datum);
        msg = msg.replaceAll("%ID", id.toString());
        return msg;
    }

    private String createDynamicMailContent_BossToApproveRequest(Long id, String antragsteller, String vorgesetzter, String datum, String gruppen, String sonstiges, String approveCode, String disapproveCode) {
        String approveUrL = "http://localhost:8081/approve?code=" + approveCode;
        String disapproveUrL = "http://localhost:8081/disapprove?code=" + disapproveCode;
        String msg = StaticEmailText.SEND_TO_BOSS;
        msg = msg.replaceAll("%ID", id.toString());
        msg = msg.replaceAll("%Antragsteller", antragsteller); // Antragsteller
        msg = msg.replaceAll("%Vorgesetzter", vorgesetzter); // Vorgesetzter
        msg = msg.replaceAll("%Datum", datum); // Datum
        msg = msg.replaceAll("%Tabelle", HTML_table_builder(gruppen)); // table
        msg = msg.replaceAll("%Sonstiges", sonstiges);
        msg = msg.replaceAll("%link1", approveUrL); // link1
        msg = msg.replaceAll("%link2", disapproveUrL); // link2
        return msg;
    }

    private String createDynamicMailContent_ApproveInfo(Long id, String requestingPerson, String approver, String nextapprover, String datum, String msg) {
        msg = msg.replaceAll("%Antragsteller", requestingPerson);
        msg = msg.replaceAll("%Vorgesetzter", approver);
        msg = msg.replaceAll("%BossVonBoss", nextapprover);
        msg = msg.replaceAll("%Datum", datum);
        msg = msg.replaceAll("%ID", id.toString());
        return msg;
    }

    private String createDynamicMailContent_Quittung(Long id, String antragsteller, String vorgesetzter, String datum, String gruppen, String sonstiges) {
        String msg = StaticEmailText.QUITTUNG_FOR_EMPLOYEE;

        msg = msg.replaceAll("%ID", id.toString());
        msg = msg.replaceAll("%Antragsteller", antragsteller); // Antragsteller
        msg = msg.replaceAll("%Vorgesetzter", vorgesetzter); // Vorgesetzter
        msg = msg.replaceAll("%Datum", datum); // Datum
        msg = msg.replaceAll("%Tabelle", HTML_table_builder(gruppen)); // table
        msg = msg.replaceAll("%Sonstiges", sonstiges);
        return msg;
    }

    private String createDynamicMailContent_FinalApproveInfo(Long id, String antragsteller, String datum, String msg) {
        msg = msg.replaceAll("%ID", id.toString());
        msg = msg.replaceAll("%Antragsteller", antragsteller); // Antragsteller
        msg = msg.replaceAll("%Vorgesetzter", GF_NAME); // Vorgesetzter
        msg = msg.replaceAll("%Datum", datum); // Datum
        return msg;
    }

    private String createDynamicMailContent_EDV(Long id, String antragsteller, String datum, String sonstiges, String gruppen) {
        String msg = StaticEmailText.EDV_MAIL;
        msg = msg.replaceAll("%ID", id.toString());
        msg = msg.replaceAll("%Antragsteller", antragsteller); // Antragsteller
        msg = msg.replaceAll("%Vorgesetzter", GF_NAME); // Vorgesetzter
        msg = msg.replaceAll("%Datum", datum); // Datum
        msg = msg.replaceAll("%Sonstiges", sonstiges);
        msg = msg.replaceAll("%Tabelle", HTML_table_builder(gruppen)); // table
        return msg;
    }


    public String HTML_table_builder(String dataBaseEntry) {
        //tbody, thead und tr müssen nicht gestyled werden
        String tdStyle = " style=\"line-height: 24px; font-size: 16px; margin: 0; padding: 12px; border: 1px solid #e2e8f0;\" align=\"left\" valign=\"top\"";
        String thStyle = " style=\"line-height: 24px; font-size: 16px; margin: 0; padding: 12px; border-color: #e2e8f0; border-style: solid; border-width: 1px 1px 2px;\" align=\"left\" valign=\"top\"";
        String[] groups = dataBaseEntry.split("#");

        StringBuilder tablehead = new StringBuilder();
        tablehead.append("<thead><tr><th").append(thStyle).append(">Name</th><th").append(thStyle).append(">Beschreibung</th><th").append(thStyle).append(">Kategorie</th></tr></thead>");
        tablehead.append("<tbody>");
        StringBuilder tableRows = new StringBuilder();

        for (String group : groups) {
            String[] properties = group.split("&");
            String name = properties[0].split(":")[1];
            String beschreibung = properties[1].split(":")[1];
            String kategorie = properties[2].split(":")[1]; // Extract Kategorie value

            String tableRow = "<tr><td" + tdStyle + ">" + name + "</td><td" + tdStyle + ">" + beschreibung + "</td><td" + tdStyle + ">" + kategorie + "</td></tr>";
            tableRows.append(tableRow);
        }
        tablehead.append(tableRows);
        tablehead.append("</tbody>");
        return tablehead.toString();
    }

    public String gruppenObjectToDatabaseString(List<ZWW_Authority> zwwAuthorityList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ZWW_Authority authority : zwwAuthorityList) {
            String name = "Name:" + authority.getName();
            String description = "Beschreibung:" + authority.getDescription();
            String category = "Kategorie:" + authority.getCategory();

            String authorityString = name + "&" + description + "&" + category + "#";

            stringBuilder.append(authorityString);
        }

        String result = stringBuilder.toString();

        // Remove the trailing '#' if needed
        if (result.endsWith("#")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private String whichBossToSendNext_Mail(PermissionRequest anfrage) {
        String result = "testes@wasserwerke-westerzgebirge.de";
        if (anfrage.getApprovedLevels() == 1) result = anfrage.getBoss2Mail();
        if (anfrage.getApprovedLevels() == 2) result = anfrage.getBoss3Mail();
        if (anfrage.getApprovedLevels() == 3) result = anfrage.getBoss4Mail();
        return result;
    }

    private String whichBossToSendNext_Name(PermissionRequest anfrage) {
        String result = "Nächste Boss Name";
        if (anfrage.getApprovedLevels() == 1) result = anfrage.getBoss2DisplayName();
        if (anfrage.getApprovedLevels() == 2) result = anfrage.getBoss3DisplayName();
        if (anfrage.getApprovedLevels() == 3) result = anfrage.getBoss4DisplayName();
        return result;
    }

    private int whichDateToSet(PermissionRequest anfrage) {
        int result = 4;
        if (anfrage.getApprovedLevels() == 1) result = 1;
        if (anfrage.getApprovedLevels() == 2) result = 2;
        if (anfrage.getApprovedLevels() == 3) result = 3;
        return result;
    }

    private String whichBossApproved_name(PermissionRequest anfrage) {
        String approver = "Zustimmer_name";
        if (anfrage.getApprovedLevels() == 1) approver = anfrage.getBoss1DisplayName();
        if (anfrage.getApprovedLevels() == 2) approver = anfrage.getBoss2DisplayName();
        return approver;
    }

    private String whichBossApproved_mail(PermissionRequest anfrage) {
        String approver = "Zustimmer_mail";
        if (anfrage.getApprovedLevels() == 1) approver = anfrage.getBoss1Mail();
        if (anfrage.getApprovedLevels() == 2) approver = anfrage.getBoss2Mail();
        if (anfrage.getApprovedLevels() == 3) approver = anfrage.getBoss3Mail();
        return approver;
    }

    private String whichBossCanceled_Name(PermissionRequest anfrage) {
        String disapprover_name = " ";
        if (anfrage.getApprovedLevels() == 0) disapprover_name = anfrage.getBoss1DisplayName();
        if (anfrage.getApprovedLevels() == 1) disapprover_name = anfrage.getBoss2DisplayName();
        if (anfrage.getApprovedLevels() == 2) disapprover_name = anfrage.getBoss3DisplayName();
        if (anfrage.getApprovedLevels() == 3) disapprover_name = anfrage.getBoss4DisplayName();
        return disapprover_name;
    }

    private String whichBossCanceled_Mail(PermissionRequest anfrage) {
        String disapprover_mail = " ";
        if (anfrage.getApprovedLevels() == 0) disapprover_mail = anfrage.getBoss1Mail();
        if (anfrage.getApprovedLevels() == 1) disapprover_mail = anfrage.getBoss2Mail();
        if (anfrage.getApprovedLevels() == 2) disapprover_mail = anfrage.getBoss3Mail();
        if (anfrage.getApprovedLevels() == 3) disapprover_mail = anfrage.getBoss4Mail();
        return disapprover_mail;
    }
}
