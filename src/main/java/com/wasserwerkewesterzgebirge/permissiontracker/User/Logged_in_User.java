package com.wasserwerkewesterzgebirge.permissiontracker.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Collection;

@NoArgsConstructor
@Data
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


