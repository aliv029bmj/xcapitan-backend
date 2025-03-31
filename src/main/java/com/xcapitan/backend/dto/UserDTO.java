package com.xcapitan.backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private int points;
    private int coins;
    private int level;
    private String badges;
}