package com.xcapitan.backend.controller;

import com.xcapitan.backend.dto.AnswerDTO;
import com.xcapitan.backend.entity.Answer;
import com.xcapitan.backend.repository.AnswerRepository;
import com.xcapitan.backend.service.AnswerService;
import com.xcapitan.backend.service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private GamificationService gamificationService;

    @PostMapping
    public ResponseEntity<AnswerDTO> createAnswer(@RequestBody AnswerDTO dto) {
        AnswerDTO answer = answerService.createAnswer(dto);
        gamificationService.awardPoints(answer.getUserId(), answer.getAiScore());
        gamificationService.awardBadge(answer.getUserId(), "First Answer", "Posted your first answer!");
        return ResponseEntity.ok(answer);
    }

    @GetMapping
    public ResponseEntity<Page<AnswerDTO>> getAnswersByQuestionId(
            @RequestParam Long questionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AnswerDTO> answers = answerRepository.findByQuestionId(questionId, pageable).map(a -> {
            AnswerDTO dto = new AnswerDTO();
            dto.setId(a.getId());
            dto.setContent(a.getContent());
            dto.setCodeSnippet(a.getCodeSnippet());
            dto.setQuestionId(a.getQuestion().getId());
            dto.setUserId(a.getUser().getId());
            dto.setCreatedAt(a.getCreatedAt());
            dto.setAiScore(a.getAiScore());
            return dto;
        });
        return ResponseEntity.ok(answers);
    }
}