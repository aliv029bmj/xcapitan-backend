package com.xcapitan.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDTO {
    private Long id;
    private String content;
    private String codeSnippet;
    private Long questionId;
    private Long userId;
    private LocalDateTime createdAt;
    private int aiScore;
}