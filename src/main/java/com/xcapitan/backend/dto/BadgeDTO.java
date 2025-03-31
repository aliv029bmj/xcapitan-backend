package com.xcapitan.backend.dto;

import lombok.Data;

@Data
public class BadgeDTO {
    private Long id;
    private String name;
    private String description;
    private Long userId;
}