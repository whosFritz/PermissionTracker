package com.wasserwerkewesterzgebirge.permissiontracker.Security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.wasserwerkewesterzgebirge.permissiontracker.User.ActiveDirectoryObjectMapper;
import com.wasserwerkewesterzgebirge.permissiontracker.Views.LoginView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends VaadinWebSecurity {

    @Value("${ldap.server.url}")
    private String url;
    @Value("${ldap.server.domain}")
    private String domain;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/public/**"))
                .permitAll();
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    protected void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public ActiveDirectoryObjectMapper customUserDetailsContextMapper() {
        return new ActiveDirectoryObjectMapper();
    }

    @Bean
    ActiveDirectoryLdapAuthenticationProvider authenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider AD_ldap_auth_provider = new ActiveDirectoryLdapAuthenticationProvider(
                domain,
                url);
        AD_ldap_auth_provider.setUserDetailsContextMapper(customUserDetailsContextMapper());
        return AD_ldap_auth_provider;
    }
}


