package org.endofusion.endoserver.response;

import org.endofusion.endoserver.dto.UserDto;
import org.endofusion.endoserver.request.GenericRequest;

public class UserResponse extends GenericRequest {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private RoleDTO role;
    private Boolean status;
    private Boolean locked;

    public UserResponse(UserDto userDto) {
        this.setId(userDto.getId());
        this.setUserId(userDto.getUserId());
        this.setUsername(userDto.getUsername());
        this.setFirstName(userDto.getFirstName());
        this.setLastName(userDto.getLastName());
        this.setEmail(userDto.getEmail());
        this.setRole(userDto.getRole());
        this.setStatus(userDto.getStatus());
        this.setLocked(userDto.getLocked());
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

    public void setRole(RoleDTO role) {
        this.role =  role;
    }

    public RoleDTO getRole() {
        return role;
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
}
