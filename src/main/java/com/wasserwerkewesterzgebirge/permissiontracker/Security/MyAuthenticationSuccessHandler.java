package com.wasserwerkewesterzgebirge.permissiontracker.Security;

import com.wasserwerkewesterzgebirge.permissiontracker.User.Logged_in_User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    Logger logger = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Logged_in_User logged_in_user = (Logged_in_User) event.getAuthentication().getPrincipal();
        logger.info("User wurde eingeloggt: " + logged_in_user.getGanzer_Name());
    }
}
