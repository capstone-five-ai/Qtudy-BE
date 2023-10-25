package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.AiGenerateDto;
import com.app.domain.file.problem.service.AiGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ai")
public class AiGenerateController {
    @Autowired
    AiGenerateService aiGenerateService;

    @PostMapping("/generateQuestion")
    public ResponseEntity<String> createQuestion(@Valid @RequestBody AiGenerateDto aiGenerateDto) {
        return aiGenerateService.AiGenerateProblem(aiGenerateDto);
    }







}
