package com.wasserwerkewesterzgebirge.permissiontracker.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Collection;

/**
 * This class is used to store the logged-in user.
 * It extends the LdapUserDetailsImpl class from the spring security ldap module.
 * It also stores the bosses of the user.
 * The bosses are stored as LdapUser objects.
 * The count_bosses variable is used to store the number of bosses the user has.
 * The passwordExpireDate variable is used to store the date when the password of the user expires.
 * The users_authorities variable is beeing used to store the authorities of the user.
 * The DN variable is used to store the DN of the user.
 * The ganzer_Name variable is beeing used to store the full name of the user.
 * The nachname variable is used to store the last name of the user.
 * The vorname variable is used to store the first name of the user.
 * The kuerzel variable is used to store the short name of the user.
 * The mail variable is beeing used to store the email address of the user.
 */
@NoArgsConstructor
@Getter
@Setter
public class Logged_in_User extends LdapUserDetailsImpl {
    private String kuerzel;
    private String vorname;
    private String nachname;
    private String ganzer_Name;
    private String DN;
    private String mail;
    private Collection<? extends GrantedAuthority> users_authorities;
    private String passwordExpireDate;
    private int count_bosses;
    private LdapUser boss1;
    private LdapUser boss2;
    private LdapUser boss3;
    private LdapUser boss4;
}


