package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * LoginView is the view for the login page.
 * It is the first page the user sees when he opens the application.
 * It is also the page the user is redirected to when he is not logged in and tries to access a page that requires authentication.
 * The user can log in with his username and password.
 * If the user is not logged in and tries to access a page that requires authentication, he is redirected to the login page.
 * If the user is logged in and tries to access the login page, he is redirected to the main page.
 */
@Route("login")
@PageTitle("Login | PermTracker")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterListener {
    private final LoginForm loginForm = new LoginForm();
    Logger logger = LoggerFactory.getLogger(LoginView.class);


    public LoginView(@Value("${ldap.domain.name}") String domainName) {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        StreamResource logoStream = new StreamResource("Logo.png", () -> getClass().getResourceAsStream("/static/img/Logo.png"));
        Image logoImage = new Image(logoStream, "Logo");
        logoImage.setAlt("Logo");
        logoImage.setWidth("20%");
        logoImage.setHeight("auto");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Melde dich an");
        i18nForm.setUsername(domainName + "\\" + "...");
        i18nForm.setPassword("Dein Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Passwort vergessen?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Fehler");
        i18nErrorMessage.setMessage(
                "Fehler text");
        i18n.setErrorMessage(i18nErrorMessage);

        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(true);
        loginForm.addForgotPasswordListener(event -> Notification.show("Wenden Sie sich dafür bitte an Ihren Administrator.❤️"));
        loginForm.addLoginListener(event -> logger.info("User versuchte sich einzuloggen mit Benutzername: " + event.getUsername()));
        loginForm.setI18n(i18n);

        add(
                logoImage,
                loginForm
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent
                .getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(false);
        }
    }
}

