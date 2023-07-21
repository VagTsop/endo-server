package org.endofusion.endoserver.dto;

import org.endofusion.endoserver.request.GenericRequest;
import org.endofusion.endoserver.request.UserRequest;
import org.endofusion.endoserver.response.RoleDTO;

public class UserDto extends GenericRequest {

    private String userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private RoleDTO role;

    private Boolean status;

    private Boolean locked;

    public UserDto() {
    }

    public UserDto(String userId, String username, String firstName, String lastName, String email, Boolean status, Boolean locked) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.locked = locked;
    }

    public UserDto(String userId, String username, String firstName, String lastName, String email, RoleDTO role, Boolean status, Boolean locked) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.locked = locked;
    }

    public UserDto(UserRequest request, Long id, boolean isUpdate) {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setUserId(request.getUserId());
        this.setFirstName(request.getFirstName());
        this.setLastName(request.getLastName());
        this.setUsername(request.getUsername());
        this.setEmail(request.getEmail());
        this.setRole(request.getRole());
        this.setPassword(request.getPassword());
        this.setStatus(request.getStatus());
        this.setLocked(request.getLocked());
        if (isUpdate) {
            this.setId(id);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) { this.role = role;}
}
