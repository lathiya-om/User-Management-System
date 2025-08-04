package com.exhibyt.UserManagment.Dto;


import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
}
