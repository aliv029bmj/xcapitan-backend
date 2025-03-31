package com.xcapitan.backend.controller;

import com.xcapitan.backend.dto.UserDTO;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getLeaderboard() {
        List<UserDTO> leaderboard = userRepository.findAll().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()))
                .limit(10)
                .map(u -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(u.getId());
                    dto.setUsername(u.getUsername());
                    dto.setPoints(u.getPoints());
                    dto.setLevel(u.getLevel());
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(leaderboard);
    }
}