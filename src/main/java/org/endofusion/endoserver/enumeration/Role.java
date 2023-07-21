package org.endofusion.endoserver.enumeration;

import static org.endofusion.endoserver.constant.Authority.*;

public enum Role {
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_USER(USER_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}