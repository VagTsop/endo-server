package org.endofusion.endoserver.response;

public class RoleDTO {
    private String name;
    private String[] authorities;

    // Constructors (default and parameterized)
    public RoleDTO() {
    }

    public RoleDTO(String name, String[] authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
}
