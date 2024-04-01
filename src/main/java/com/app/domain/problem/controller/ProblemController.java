package com.app.domain.problem.controller;

import com.app.domain.problem.dto.Problem.Response.*;
import com.app.domain.problem.dto.ProblemFile.Response.AiGeneratedProblemList;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problem")
public class ProblemController { // Controller 추후 분할 예정
    @Autowired
    ProblemService problemService;

    @GetMapping("/getFileProblems/{fileId}") // 파일의 전체 문제정보를 가져옴
    public ResponseEntity<ProblemResponseDto> GetFileProblems(HttpServletRequest httpServletRequest, @PathVariable int fileId) {
        List<AiGeneratedProblem> problems = problemService.GetFileProblems(fileId);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        // "Authorization" 헤더가 존재하면 checkIsWriter 함수 호출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = problemService.checkIsWriter(httpServletRequest,fileId); // 인증 변수추가
        }
        //boolean isWriter = problemService.checkIsWriter(httpServletRequest,fileId); // 인증 변수추가

        List<AiGeneratedProblemList> responseDtos = problems.stream()
                .map(AiGeneratedProblemList::ConvertToProblem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ProblemResponseDto.ConvertToProblem(isWriter,responseDtos)); // 인증변수 반환
    }

    /*@GetMapping("/getProblem/{aiGeneratedProblemId}") // 파일의 단일 문제정보를 가져옴 (삭제 예정)
    public ResponseEntity<ProblemResponseDto> GetProblem(@RequestHeader("Authorization") String token, @PathVariable int aiGeneratedProblemId) {
        AiGeneratedProblem aiGeneratedProblem = problemService.GetProblem(token, aiGeneratedProblemId);

        ProblemResponseDto responseDto = ProblemResponseDto.ConvertToProblem(aiGeneratedProblem);

        return ResponseEntity.ok(responseDto);
    }*/




}

