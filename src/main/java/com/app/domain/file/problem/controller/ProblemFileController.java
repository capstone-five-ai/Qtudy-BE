package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.file.problem.dto.ProblemFile.Request.DeleteProblemFileDto;
import com.app.domain.file.problem.service.ProblemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/problem")
public class ProblemFileController { // Controller 추후 분할 예정
    @Autowired
    ProblemFileService problemFileService;

    @PostMapping("/generateProblemFile") // 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFile(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemDto aiproblemDtoGenerate) {
        problemFileService.AiGenerateProblemFile(token, aiproblemDtoGenerate);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/deleteProblemFile") // 문제파일 삭제
    public ResponseEntity<String> deleteProblemFile(@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteProblemFileDto deleteProblemFileDto){
        problemFileService.DeleteProblemFile(token,deleteProblemFileDto);

        return ResponseEntity.ok("Sucess");
    }


}

