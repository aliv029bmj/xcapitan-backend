package com.xcapitan.backend.controller;

import com.xcapitan.backend.dto.UserDTO;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPoints(user.getPoints());
        dto.setCoins(user.getCoins());
        dto.setLevel(user.getLevel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = userService.getCurrentUser();
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPoints(user.getPoints());
        dto.setCoins(user.getCoins());
        dto.setLevel(user.getLevel());
        return ResponseEntity.ok(dto);
    }
}