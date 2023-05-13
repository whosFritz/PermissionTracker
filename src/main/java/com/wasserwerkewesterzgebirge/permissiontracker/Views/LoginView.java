package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
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

@Route("login")
@PageTitle("Login | PermTracker")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        StreamResource logoStream = new StreamResource("icon.png", () -> getClass().getResourceAsStream("/static/img/Icon.png"));
        Image logoImage = new Image(logoStream, "Logo");
        logoImage.setAlt("LOGO");
        logoImage.setWidth("20%");
        logoImage.setHeight("auto");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Melde dich an");
        i18nForm.setUsername("wasserwerke" + '/' + "...");
        i18nForm.setPassword("Dein Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Passwort vergessen?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Da trat ein Fehler auf");
        i18nErrorMessage.setMessage(
                "Fehler text");
        i18n.setErrorMessage(i18nErrorMessage);


        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(true);
        loginForm.addForgotPasswordListener(event -> Notification.show("Wenden Sie sich dafür bitte an Ihren Administrator.❤\uFE0F"));
        loginForm.setI18n(i18n);

        Span info = new Span("Diese Anwendung wird ausschließlich für kommerzielle Zwecke genutzt!");
        add(
                logoImage,
                loginForm,
                info
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent
                .getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}

