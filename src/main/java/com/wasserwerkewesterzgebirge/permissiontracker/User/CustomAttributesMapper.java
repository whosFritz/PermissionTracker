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

        ldapUser.setDN(attributes.get("distinguishedName") != null ? (String) attributes.get("distinguishedName").get() : null);
        ldapUser.setDisplayName(attributes.get("displayname") != null ? (String) attributes.get("displayname").get() : null);
        ldapUser.setEmail(attributes.get("mail") != null ? (String) attributes.get("mail").get() : null);
        ldapUser.setChef(attributes.get("manager") != null ? (String) attributes.get("manager").get() : null);

        return ldapUser;
    }
}
