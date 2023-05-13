package com.wasserwerkewesterzgebirge.permissiontracker.User;

import com.wasserwerkewesterzgebirge.permissiontracker.data.entities.ZWW_Authority;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class Logged_in_User extends LdapUserDetailsImpl {
    private String kuerzel;
    private String vorname;
    private String nachname;
    private String ganzer_Name; //displayname
    private String DN;
    private String mail;
    private Collection<? extends GrantedAuthority> users_authorities;
    private List<ZWW_Authority> matchingAuthorities;
    private int count_bosses;
    private LdapUser boss1;
    private LdapUser boss2;
    private LdapUser boss3;
}


