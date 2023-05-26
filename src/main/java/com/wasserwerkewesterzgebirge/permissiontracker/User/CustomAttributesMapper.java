package com.wasserwerkewesterzgebirge.permissiontracker.User;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * CustomAttributesMapper
 * <p>
 * Maps the attributes from the LDAP server to the LdapUser object
 * </p>
 */
public class CustomAttributesMapper implements AttributesMapper<LdapUser> {

    @Override
    public LdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        LdapUser ldapUser = new LdapUser();

        ldapUser.setDN((String) attributes.get("distinguishedName").get());
        ldapUser.setDisplayName((String) attributes.get("displayname").get());
        ldapUser.setEmail((String) attributes.get("mail").get());
        ldapUser.setChef((String) attributes.get("manager").get());

        return ldapUser;
    }
}
