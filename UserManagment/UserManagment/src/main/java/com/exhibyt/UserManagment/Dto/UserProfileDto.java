package com.exhibyt.UserManagment.Dto;



public class UserProfileDto {
    private String username;
    private String email;
    private boolean active;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserProfileDto() {
    }

    public UserProfileDto(String username, String email, boolean active) {
        this.username = username;
        this.email = email;
        this.active = active;
    }
}
