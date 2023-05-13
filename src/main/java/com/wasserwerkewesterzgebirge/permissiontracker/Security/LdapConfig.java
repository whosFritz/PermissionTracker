package com.wasserwerkewesterzgebirge.permissiontracker.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Value("${ldap.url}")
    private String ldapUrl;
    @Value("${ldap.Admin.Dn}")
    private String ldapAdminDn;
    @Value("${ldap.Admin.password}")
    private String ldapAdminPassword;

    @Bean
    public LdapContextSource init_ldap_contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setUserDn(ldapAdminDn);
        contextSource.setPassword(ldapAdminPassword);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(init_ldap_contextSource());
    }


}
