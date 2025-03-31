package com.xcapitan.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String content;
    private String codeSnippet;
    private Long userId;
    private LocalDateTime createdAt;
    private List<String> tags;
}