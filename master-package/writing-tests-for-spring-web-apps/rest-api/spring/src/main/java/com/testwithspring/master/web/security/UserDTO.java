package com.testwithspring.master.web.security;

import com.testwithspring.master.user.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Contains the information of the authenticated user that
 * is returned back to the client.
 */
public class UserDTO {

    private final String username;
    private final UserRole role;

    UserDTO(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;

        GrantedAuthority authority = authorities.iterator().next();
        this.role = UserRole.valueOf(authority.getAuthority());
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
