package com.xcapitan.backend.service;

import com.xcapitan.backend.dto.AnswerDTO;
import com.xcapitan.backend.entity.Answer;
import com.xcapitan.backend.entity.Question;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.repository.AnswerRepository;
import com.xcapitan.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    // Gemini API açarını application.properties-dən oxumaq
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    // Gemini API endpoint-i (nümunə, real endpoint Gemini sənədlərindən alınmalıdır)
    private static final String GEMINI_API_URL = "https://api.google.com/gemini/v1/evaluate";

    public AnswerDTO createAnswer(AnswerDTO dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));
        User user = userService.getCurrentUser();

        Answer answer = new Answer();
        answer.setContent(dto.getContent());
        answer.setCodeSnippet(dto.getCodeSnippet());
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setAiScore(calculateAiScore(answer));

        answer = answerRepository.save(answer);

        dto.setId(answer.getId());
        dto.setUserId(user.getId());
        dto.setCreatedAt(answer.getCreatedAt());
        dto.setAiScore(answer.getAiScore());
        return dto;
    }

    private int calculateAiScore(Answer answer) {
        try {
            // Gemini API-yə göndəriləcək request gövdəsi
            String requestBody = String.format(
                    "{\"text\": \"%s\", \"context\": \"%s\"}",
                    answer.getContent(),
                    answer.getQuestion().getContent()
            );

            // Header-lərə API açarını əlavə edirik
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + geminiApiKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Gemini API-yə POST sorğusu
            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Cavabdan skoru almaq
            String responseBody = response.getBody();
            return extractScoreFromResponse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return 50; // Xəta olarsa default skor
        }
    }

    private int extractScoreFromResponse(String responseBody) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBody);
        int score = jsonNode.get("score").asInt(50); // Default 50, əgər score yoxdursa
        return Math.max(0, Math.min(100, score)); // 0-100 arası normallaşdırma
    }
}