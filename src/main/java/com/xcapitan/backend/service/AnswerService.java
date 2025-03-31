package com.xcapitan.backend.service;

import com.xcapitan.backend.dto.AnswerDTO;
import com.xcapitan.backend.entity.Answer;
import com.xcapitan.backend.entity.Question;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.repository.AnswerRepository;
import com.xcapitan.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        String aiServiceUrl = "https://mock-ai-service.com/score";
        String response = restTemplate.postForObject(aiServiceUrl, answer.getContent(), String.class);
        return Integer.parseInt(response != null ? response : "50");
    }
}