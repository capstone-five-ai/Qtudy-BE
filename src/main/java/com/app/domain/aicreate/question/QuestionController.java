package com.app.domain.aicreate.question;

import com.app.domain.aicreate.ENUM.GptType;
import com.app.domain.aicreate.dto.QuestionDto;
import com.example.demo.aicreate.ENUM.GptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @PostMapping("/createQuestion")
    public String createQuestion(@RequestBody QuestionDto qeustionDto) {
        return questionService.createQuestion(questionDto);
    }







}
