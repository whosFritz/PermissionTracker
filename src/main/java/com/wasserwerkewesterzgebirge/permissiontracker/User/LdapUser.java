package com.wasserwerkewesterzgebirge.permissiontracker.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LdapUser {
    private String DN;
    private String displayName;
    private String email;
    private String chef;


}
