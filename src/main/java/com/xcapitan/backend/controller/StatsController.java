package com.xcapitan.backend.controller;

import com.xcapitan.backend.repository.AnswerRepository;
import com.xcapitan.backend.repository.QuestionRepository;
import com.xcapitan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalAnswers", answerRepository.count());
        stats.put("activeUsers", userRepository.count());
        stats.put("weeklyViews", 127429); // Bu sadəcə mock dəyərdir, real sistemdə analitik alətlərdən alınmalıdır
        return ResponseEntity.ok(stats);
    }
}