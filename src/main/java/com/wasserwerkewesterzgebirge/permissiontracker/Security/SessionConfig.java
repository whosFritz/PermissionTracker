package com.wasserwerkewesterzgebirge.permissiontracker.Security;

import com.vaadin.flow.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* @Configuration */
public class SessionConfig implements VaadinServiceInitListener, SystemMessagesProvider, SessionExpiredHandler, SessionDestroyListener {
    Logger logger = LoggerFactory.getLogger(SessionConfig.class);

    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        try {
            sessionDestroyEvent.getSession().getSession().invalidate();
        } catch (Exception e) {
            logger.error("Ein Fehler trat auf: ", e);
        }
    }

    @Override
    public boolean handleSessionExpired(VaadinRequest request, VaadinResponse response) {
        return false;
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) {
        return false;
    }

    @Override
    public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
        var messages = new CustomizedSystemMessages();
        messages.setSessionExpiredNotificationEnabled(true);
        messages.setCookiesDisabledCaption("Cookies deaktiviert");
        messages.setCookiesDisabledMessage("Bitte aktivieren Sie Cookies in Ihrem Browser.");
        messages.setSessionExpiredMessage("Die Sitzung wurde aufgrund von Inaktivität nach 1 Minute beendet!");
        messages.setSessionExpiredCaption("Sitzung abgelaufen");
        messages.setInternalErrorCaption("Interner Serverfehler");
        messages.setInternalErrorMessage("Ein interner Fehler ist aufgetreten. Bitte benachrichtigen Sie die technischen Administratoren!");
        messages.setSessionExpiredURL("http://permission-track.ww-szb.local:8081/login");
        logger.info("Session Expired");
        return messages;
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().setSystemMessagesProvider(this);
        serviceInitEvent.getSource().addSessionDestroyListener(this);
    }
}
