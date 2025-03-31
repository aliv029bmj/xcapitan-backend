package com.xcapitan.backend.service;

import com.xcapitan.backend.entity.Badge;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.repository.BadgeRepository;
import com.xcapitan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BadgeRepository badgeRepository;

    public void awardPoints(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(user.getPoints() + points);
        user.setCoins(user.getCoins() + points / 10);
        user.setLevel(calculateLevel(user.getPoints()));
        userRepository.save(user);
    }

    public void awardBadge(Long userId, String badgeName, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Badge badge = new Badge();
        badge.setName(badgeName);
        badge.setDescription(description);
        badge.setUser(user);
        badgeRepository.save(badge);
    }

    private int calculateLevel(int points) {
        return (int) (Math.sqrt(points) / 10) + 1;
    }
}