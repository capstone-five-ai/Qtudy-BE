package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.AiProblemDto;
import com.app.domain.file.problem.service.AiGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/problem")
public class AiProblemController {
    @Autowired
    AiGenerateService aiGenerateService;

    @PostMapping("/generateProblem")
    public ResponseEntity<String> AiGenerateProblem(@Valid @RequestBody AiProblemDto aiproblemDto) {
        return aiGenerateService.AiGenerateProblem(aiproblemDto);
    }







}
