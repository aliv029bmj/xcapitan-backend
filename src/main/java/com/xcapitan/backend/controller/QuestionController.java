package com.xcapitan.backend.controller;

import com.xcapitan.backend.dto.QuestionDTO;
import com.xcapitan.backend.entity.Question;
import com.xcapitan.backend.repository.QuestionRepository;
import com.xcapitan.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO dto) {
        return ResponseEntity.ok(questionService.createQuestion(dto));
    }

    @GetMapping
    public ResponseEntity<Page<QuestionDTO>> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionDTO> questions = questionRepository.findAll(pageable).map(q -> {
            QuestionDTO dto = new QuestionDTO();
            dto.setId(q.getId());
            dto.setTitle(q.getTitle());
            dto.setContent(q.getContent());
            dto.setCodeSnippet(q.getCodeSnippet());
            dto.setUserId(q.getUser().getId());
            dto.setCreatedAt(q.getCreatedAt());
            dto.setTags(q.getTags());
            return dto;
        });
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setTitle(q.getTitle());
        dto.setContent(q.getContent());
        dto.setCodeSnippet(q.getCodeSnippet());
        dto.setUserId(q.getUser().getId());
        dto.setCreatedAt(q.getCreatedAt());
        dto.setTags(q.getTags());
        return ResponseEntity.ok(dto);
    }
}