package com.xcapitan.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role = "USER"; // USER or ADMIN
    private int points = 0;
    private int coins = 0;
    private int level = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Badge> badges;
}