package com.tique.dev.rest.model;

import org.springframework.security.core.GrantedAuthority;

public enum RoleUser implements GrantedAuthority {
   ROLE_CLIENT, ROLE_ADMIN ;

    @Override
    public String getAuthority() {
        return name();
    }
}
