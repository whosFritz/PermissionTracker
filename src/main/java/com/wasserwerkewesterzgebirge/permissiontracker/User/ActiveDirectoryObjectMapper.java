package com.wasserwerkewesterzgebirge.permissiontracker.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class ActiveDirectoryObjectMapper extends LdapUserDetailsMapper {
    private int bossCount = 0;
    @Value("${mail.gf.name}")
    private String GF_NAME;
    @Autowired
    private LdapTemplate ldapTemplate;

    public String calculatePasswordExpirationDate(String pwdLastSet) {
        // Replace this with the actual pwdLastSet value
        int maxPasswordAgeInDays = 90; // Replace this with the maximum password age in your Active Directory

        // Convert pwdLastSet to milliseconds
        long pwdLastSetMillis = Long.parseLong(pwdLastSet) / 10000 - 11644473600000L;

        // Create a Calendar object and set the pwdLastSet date
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pwdLastSetMillis);

        // Add the maximum password age to the pwdLastSet date
        calendar.add(Calendar.DAY_OF_MONTH, maxPasswordAgeInDays);

        // Get the expiration date
        Date expirationDate = calendar.getTime();

        return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(expirationDate);
    }

    public LdapUser findLdapUser(String dn) {
        return (LdapUser) ldapTemplate.lookup(dn, new CustomAttributesMapper());
    }


    @Override
    public Logged_in_User mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

        // Delegate to the default mapper for some attributes
        UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);
        // set directly from ctx
        Logged_in_User loggedInUser = new Logged_in_User();

        loggedInUser.setKuerzel(userDetails.getUsername()); // kürzel
        loggedInUser.setVorname(ctx.getStringAttribute("givenName")); // Vorname
        loggedInUser.setNachname(ctx.getStringAttribute("sn")); // Nachname
        loggedInUser.setGanzer_Name(ctx.getStringAttribute("displayname")); // Vorname Nachname
        loggedInUser.setMail(ctx.getStringAttribute("mail")); // Email
        loggedInUser.setUsers_authorities(userDetails.getAuthorities()); // [Sicherheitsgruppen]
        loggedInUser.setDN(ctx.getStringAttribute("distinguishedName")); // CN=DisplayName, OU=...
        loggedInUser.setPasswordExpireDate(calculatePasswordExpirationDate(ctx.getStringAttribute("pwdLastSet")));


        // wenn chef null dann setzt er die eigene dn ein, also ein loop tritt auf
        if (ctx.getStringAttribute("manager") != null) {
            if (!loggedInUser.getGanzer_Name().equals(GF_NAME)) {
                loggedInUser.setBoss1(findLdapUser(ctx.getStringAttribute("manager"))); // direkter vorgesetzter
                bossCount += 1;
                if (!loggedInUser.getBoss1().getDisplayName().equals(GF_NAME)) {
                    // Zweite Ebene
                    loggedInUser.setBoss2(findLdapUser(loggedInUser.getBoss1().getChef())); // chef vom chef
                    bossCount += 1;
                    if (!loggedInUser.getBoss2().getDisplayName().equals(GF_NAME)) {
                        // Dritte Ebene
                        loggedInUser.setBoss3(findLdapUser(loggedInUser.getBoss2().getChef())); // chef vom chef vom chef
                        bossCount += 1;
                        if (!loggedInUser.getBoss3().getDisplayName().equals(GF_NAME)) {
                            // Vierte Ebene
                            loggedInUser.setBoss4(findLdapUser(loggedInUser.getBoss3().getChef())); // chef vom chef vom chef vom chef
                            bossCount += 1;
                        }
                    }
                }
            }
        }
        loggedInUser.setCount_bosses(bossCount);
        return loggedInUser;
    }
}