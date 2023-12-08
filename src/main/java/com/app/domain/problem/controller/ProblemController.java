package com.app.domain.problem.controller;

import com.app.api.login.dto.OauthLoginDto;
import com.app.domain.problem.dto.Problem.Response.*;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problem")
public class ProblemController { // Controller 추후 분할 예정
    @Autowired
    ProblemService problemService;

    @GetMapping("/getFileProblems/{fileId}") // 파일의 전체 문제정보를 가져옴
    public ResponseEntity<List<ProblemResponseDto>> GetFileProblems( @PathVariable int fileId) {
        List<AiGeneratedProblem> problems = problemService.GetFileProblems( fileId);

        List<ProblemResponseDto> responseDtos = problems.stream()
                .map(ProblemResponseDto::ConvertToProblem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/getProblem/{aiGeneratedProblemId}") // 파일의 단일 문제정보를 가져옴 (삭제 예정)
    public ResponseEntity<ProblemResponseDto> GetProblem(@RequestHeader("Authorization") String token, @PathVariable int aiGeneratedProblemId) {
        AiGeneratedProblem aiGeneratedProblem = problemService.GetProblem(token, aiGeneratedProblemId);

        ProblemResponseDto responseDto = ProblemResponseDto.ConvertToProblem(aiGeneratedProblem);

        return ResponseEntity.ok(responseDto);
    }




}

