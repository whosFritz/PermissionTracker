package com.wasserwerkewesterzgebirge.permissiontracker.Security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.wasserwerkewesterzgebirge.permissiontracker.User.Logged_in_User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * This class is used to get the logged in user.
 */
@Component
public class SecurityService {

    /**
     * The logout success url.
     */
    private static final String LOGOUT_SUCCESS_URL = "/";

    /**
     * This method returns the logged in user.
     *
     * @return the logged in user.
     */
    public Logged_in_User getLoggedInUser() {
        return (Logged_in_User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * This method logs the user out.
     */
    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

}
