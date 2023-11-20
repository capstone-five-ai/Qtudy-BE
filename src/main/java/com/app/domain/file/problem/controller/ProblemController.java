package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.Problem.Request.FileNameRequestDto;
import com.app.domain.file.problem.dto.Problem.Response.FileNameResponseDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/problem")
public class ProblemController { // Controller 추후 분할 예정
    @Autowired
    ProblemService problemService;

    @PostMapping("/getFileProblems") // 파일의 문제리스트 가져옴
    public ResponseEntity<List<AiGeneratedProblems>> GetFileProblems(@RequestHeader("Authorization") String token, @Valid @RequestBody FileNameRequestDto fileNameRequestDto) {
        List<AiGeneratedProblems> problems = problemService.GetFileProblems(token, fileNameRequestDto);

        List<FileNameResponseDto> responseDtos = problems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(problems);
    }




    private FileNameResponseDto convertToDto(AiGeneratedProblems aiGeneratedProblems) {
        return FileNameResponseDto.builder()
                .aiGeneratedProblemId(aiGeneratedProblems.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblems.getProblemName())
                .problemChoices(aiGeneratedProblems.getProblemChoices())
                .problemAnswer(aiGeneratedProblems.getProblemAnswer())
                .problemCommentary(aiGeneratedProblems.getProblemCommentary())
                .build();
    }
}

