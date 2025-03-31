package com.xcapitan.backend.service;

import com.xcapitan.backend.dto.QuestionDTO;
import com.xcapitan.backend.entity.Question;
import com.xcapitan.backend.entity.User;
import com.xcapitan.backend.repository.QuestionRepository;
import com.xcapitan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    public QuestionDTO createQuestion(QuestionDTO dto) {
        User user = userService.getCurrentUser();
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setCodeSnippet(dto.getCodeSnippet());
        question.setUser(user);
        question.setTags(dto.getTags());

        question = questionRepository.save(question);

        dto.setId(question.getId());
        dto.setUserId(user.getId());
        dto.setCreatedAt(question.getCreatedAt());
        return dto;
    }
}