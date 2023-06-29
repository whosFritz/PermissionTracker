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

/**
 * Class to map the Active Directory User to the Logged_in_User class
 */
public class ActiveDirectoryObjectMapper extends LdapUserDetailsMapper {
    private int bossCount = 0;
    @Value("${mail.gf.name}")
    private String GF_NAME;
    @Autowired
    private LdapTemplate ldapTemplate;


    public String calculatePasswordExpirationDate(String pwdLastSet) {

        Date currentDate = new Date();
        int maxPasswordAgeInDays = 90;
        long pwdLastSetMillis = Long.parseLong(pwdLastSet) / 10000 - 11644473600000L;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pwdLastSetMillis);

        calendar.add(Calendar.DAY_OF_MONTH, maxPasswordAgeInDays);

        Date expirationDate = calendar.getTime();
        boolean isExpired = expirationDate.before(currentDate);
        System.out.println(expirationDate);
        if (isExpired) {
            return "abgelaufen";
        } else {
            return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(expirationDate);
        }
    }

    public LdapUser findLdapUser(String dn) {
        return ldapTemplate.lookup(dn, new CustomAttributesMapper());
    }


    @Override
    public Logged_in_User mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

        UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);
        Logged_in_User loggedInUser = new Logged_in_User();

        loggedInUser.setKuerzel(userDetails.getUsername() != null ? userDetails.getUsername() : null); /* kürzel */
        loggedInUser.setVorname(ctx.getStringAttribute("givenName") != null ? ctx.getStringAttribute("givenName") : null); /* Vorname */
        loggedInUser.setNachname(ctx.getStringAttribute("sn") != null ? ctx.getStringAttribute("sn") : null); /* Nachname */
        loggedInUser.setGanzer_Name(ctx.getStringAttribute("displayname") != null ? ctx.getStringAttribute("displayname") : null); /* Vorname Nachname */
        loggedInUser.setMail(ctx.getStringAttribute("mail") != null ? ctx.getStringAttribute("mail") : null); /* Email */
        loggedInUser.setUsers_authorities(userDetails.getAuthorities()); /* [Sicherheitsgruppen] */
        loggedInUser.setDN(ctx.getStringAttribute("distinguishedName") != null ? ctx.getStringAttribute("distinguishedName") : null); /* CN=DisplayName, OU=... */

//        loggedInUser.setPasswordExpireDate(calculatePasswordExpirationDate(ctx.getStringAttribute("pwdLastSet"))); // get all attributes


        if (ctx.getStringAttribute("manager") != null) {
            if (!loggedInUser.getGanzer_Name().equals(GF_NAME)) {
                loggedInUser.setBoss1(findLdapUser(ctx.getStringAttribute("manager"))); /* direkter vorgesetzter */
                bossCount += 1;
                if (loggedInUser.getBoss1() != null && !loggedInUser.getBoss1().getDisplayName().equals(GF_NAME)) {
                    /* Zweite Ebene */
                    loggedInUser.setBoss2(loggedInUser.getBoss1().getChef() != null ? findLdapUser(loggedInUser.getBoss1().getChef()) : null); /* chef vom chef */
                    bossCount += 1;
                    if (loggedInUser.getBoss2() != null && !loggedInUser.getBoss2().getDisplayName().equals(GF_NAME)) {
                        /* Dritte Ebene */
                        loggedInUser.setBoss3(loggedInUser.getBoss2().getChef() != null ? findLdapUser(loggedInUser.getBoss2().getChef()) : null); /* chef vom chef vom chef */
                        bossCount += 1;
                        if (loggedInUser.getBoss3() != null && !loggedInUser.getBoss3().getDisplayName().equals(GF_NAME)) {
                            /* Vierte Ebene */
                            loggedInUser.setBoss4(loggedInUser.getBoss3().getChef() != null ? findLdapUser(loggedInUser.getBoss3().getChef()) : null); /* chef vom chef vom chef vom chef */
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