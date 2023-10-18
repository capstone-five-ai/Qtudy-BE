package com.app.domain.aicreate.problem.controller;

import com.app.domain.aicreate.ENUM.GptType;
import com.app.domain.aicreate.problem.dto.ProblemDto;
import com.app.domain.aicreate.problem.service.QuestionService;
import com.example.demo.aicreate.ENUM.GptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @PostMapping("/createQuestion")
    public String createQuestion(@RequestBody ProblemDto qeustionDto) {
        return questionService.createQuestion(questionDto);
    }







}
