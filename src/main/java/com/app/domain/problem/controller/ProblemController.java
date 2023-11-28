package com.app.domain.problem.controller;

import com.app.domain.problem.dto.Problem.Request.CommentaryRequestDto;
import com.app.domain.problem.dto.Problem.Request.FileNameRequestDto;
import com.app.domain.problem.dto.Problem.Request.UpdateProblemChoicesRequestDto;
import com.app.domain.problem.dto.Problem.Response.FileNameResponseDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.service.ProblemService;
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

    @PostMapping("/getFileProblems") // 파일의 문제리스트 가져옴 //(추후 삭제해도될듯....??)
    public ResponseEntity<List<FileNameResponseDto>> GetFileProblems(@RequestHeader("Authorization") String token, @Valid @RequestBody FileNameRequestDto fileNameRequestDto) {
        List<AiGeneratedProblem> problems = problemService.GetFileProblems(token, fileNameRequestDto);

        List<FileNameResponseDto> responseDtos = problems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/updateProblemChoices")
    public ResponseEntity<String> UpdateProblemChoices(@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateProblemChoicesRequestDto updateProblemChoicesRequestDto){
        problemService.UpdateProblemChoices(token,updateProblemChoicesRequestDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/updateProblemCommentary")
    public ResponseEntity<String> UpdateProblemCommentary(@RequestHeader("Authorization") String token, @Valid @RequestBody CommentaryRequestDto commentaryRequestDto) {
        problemService.UpdateProblemCommentary(token, commentaryRequestDto);

        return ResponseEntity.ok("Sucess");
    }




    private FileNameResponseDto convertToDto(AiGeneratedProblem aiGeneratedProblem) {
        return FileNameResponseDto.builder()
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

