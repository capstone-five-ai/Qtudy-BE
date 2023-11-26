package com.app.domain.problem.controller;

import com.app.domain.problem.dto.Problem.Request.FileIdRequestDto;
import com.app.domain.problem.dto.Problem.Request.GetProblemRequestDto;
import com.app.domain.problem.dto.Problem.Request.UpdateProblemRequestDto;
import com.app.domain.problem.dto.Problem.Response.*;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateMultipleProblemResponseDto;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateProblemResponseDto;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateSubjectiveProblemResponseDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problem")
public class ProblemController { // Controller 추후 분할 예정
    @Autowired
    ProblemService problemService;

    @GetMapping("/getFileProblems/{fileId}") // 파일의 문제리스트 가져옴
    public ResponseEntity<List<? extends ProblemResponseDto>> GetFileProblems(@RequestHeader("Authorization") String token, @PathVariable int fileId) {
        List<AiGeneratedProblem> problems = problemService.GetFileProblems(token, fileId);

        List<ProblemResponseDto> responseDto = new ArrayList<>();
        switch(problems.get(0).getProblemType()){
            case MULTIPLE:
                responseDto = problems.stream()
                        .map(this::convertToMultiple)
                        .collect(Collectors.toList());
                break;
            case SUBJECTIVE:
                responseDto = problems.stream()
                        .map(this::convertToSubjective)
                        .collect(Collectors.toList());
        }
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/getProblem/{aiGeneratedProblemId}") // 문제의 정보를 가져옴
    public ResponseEntity<? extends ProblemResponseDto> GetProblem(@RequestHeader("Authorization") String token, @PathVariable int aiGeneratedProblemId) {
        AiGeneratedProblem aiGeneratedProblem = problemService.GetProblem(token, aiGeneratedProblemId);

        ProblemResponseDto problemResponseDto

        return ResponseEntity.ok(getProblemResponseDto);
    }

    @PatchMapping("/updateProblem/{aiGeneratedProblemId}") //문제 정보 업데이트 (객관식,주관식 추가예정)
    public ResponseEntity<UpdateProblemResponseDto> UpdateProblem(@RequestHeader("Authorization") String token,@PathVariable int aiGeneratedProblemId, @Valid @RequestBody UpdateProblemRequestDto updateProblemRequestDto){
        AiGeneratedProblem aiGeneratedProblem = problemService.UpdateProblem(token,aiGeneratedProblemId, updateProblemRequestDto);

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

    private MultipleProblemResponseDto convertToMultiple(AiGeneratedProblem aiGeneratedProblem) {
        return MultipleProblemResponseDto.multipleBuilder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemChoices(aiGeneratedProblem.getProblemChoices())
                .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .createTime(aiGeneratedProblem.getCreateTime())
                .updateTime(aiGeneratedProblem.getUpdateTime())
                .build();
    }

    private SubjectiveProblemResponseDto convertToSubjective(AiGeneratedProblem aiGeneratedProblem) {
        return SubjectiveProblemResponseDto.subjectiveBuilder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .createTime(aiGeneratedProblem.getCreateTime())
                .updateTime(aiGeneratedProblem.getUpdateTime())
                .build();
    }
}

