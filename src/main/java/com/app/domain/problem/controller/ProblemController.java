package com.app.domain.problem.controller;

import com.app.domain.problem.dto.Problem.Request.FileIdRequestDto;
import com.app.domain.problem.dto.Problem.Request.GetProblemRequestDto;
import com.app.domain.problem.dto.Problem.Request.UpdateProblemRequestDto;
import com.app.domain.problem.dto.Problem.Response.FileIdResponseDto;
import com.app.domain.problem.dto.Problem.Response.GetProblemResponseDto;
import com.app.domain.problem.dto.Problem.Response.UpdateProblemResponseDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problem")
public class ProblemController { // Controller 추후 분할 예정
    @Autowired
    ProblemService problemService;

    @PostMapping("/getFileProblems") // 파일의 문제리스트 가져옴
    public ResponseEntity<List<FileIdResponseDto>> GetFileProblems(@RequestHeader("Authorization") String token, @Valid @RequestBody FileIdRequestDto fileIdRequestDto) {
        List<AiGeneratedProblem> problems = problemService.GetFileProblems(token, fileIdRequestDto);

        List<FileIdResponseDto> responseDtos = problems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/getProblem") // 파일의 문제리스트 가져옴
    public ResponseEntity<GetProblemResponseDto> GetProblem(@RequestHeader("Authorization") String token, @Valid @RequestBody GetProblemRequestDto getProblemRequestDto) {
        AiGeneratedProblem aiGeneratedProblem = problemService.GetProblem(token, getProblemRequestDto);

        GetProblemResponseDto getProblemResponseDto = GetProblemResponseDto.builder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemChoices(aiGeneratedProblem.getProblemChoices())
                .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .build();

        return ResponseEntity.ok(getProblemResponseDto);
    }

    @PostMapping("/updateProblem") //문제 정보 업데이트
    public ResponseEntity<UpdateProblemResponseDto> UpdateProblem(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateProblemRequestDto updateProblemRequestDto){
        AiGeneratedProblem aiGeneratedProblem = problemService.UpdateProblem(token, updateProblemRequestDto);

        UpdateProblemResponseDto updateProblemResponseDto = UpdateProblemResponseDto.builder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemChoices(aiGeneratedProblem.getProblemChoices())
                .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .build();

        return ResponseEntity.ok(updateProblemResponseDto);
    }




    private FileIdResponseDto convertToDto(AiGeneratedProblem aiGeneratedProblem) {
        return FileIdResponseDto.builder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemChoices(aiGeneratedProblem.getProblemChoices())
                .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .createTime(aiGeneratedProblem.getCreateTime())
                .updateTime(aiGeneratedProblem.getUpdateTime())
                .build();
    }
}

