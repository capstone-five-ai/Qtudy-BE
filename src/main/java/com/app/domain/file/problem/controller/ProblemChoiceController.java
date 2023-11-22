package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.ProblemChoice.Request.AddChoiceRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.DeleteChoiceRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.ProblemIdRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Request.UpdateChoiceRequestDto;
import com.app.domain.file.problem.dto.ProblemChoice.Response.ProblemIdResponseDto;
import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.service.ProblemChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/problem")
public class ProblemChoiceController { // Controller 추후 분할 예정
    @Autowired
    ProblemChoiceService problemChoiceService;

    @PostMapping("/getProblemChoices") // 문제의 보기리스트 가져옴
    public ResponseEntity<List<ProblemIdResponseDto>> GetProblemChoices(@RequestHeader("Authorization") String token, @Valid @RequestBody ProblemIdRequestDto problemIdRequestDto) {
        List<AiProblemChoice> choices = problemChoiceService.GetProblemChoices(token, problemIdRequestDto);

        List<ProblemIdResponseDto> responseDtos = choices.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("addProblemChoice") // 문제의 보기 추가
        public ResponseEntity<String> AddProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody AddChoiceRequestDto addChoiceRequestDto) {
        problemChoiceService.AddProblemChoice(token, addChoiceRequestDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("deleteProblemChoice") // 문제의 보기 제거
    public ResponseEntity<String> DeleteProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteChoiceRequestDto deleteChoiceRequestDto) {
        problemChoiceService.DeleteProblemChoice(token, deleteChoiceRequestDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("updateProblemChoice") // 문제의 보기 수정
    public ResponseEntity<String> UpdateProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody UpdateChoiceRequestDto updateChoiceRequestDto) {
        problemChoiceService.UpdateProblemChoice(token, updateChoiceRequestDto);

        return ResponseEntity.ok("Sucess");
    }



    private ProblemIdResponseDto convertToDto(AiProblemChoice aiProblemChoice) {
        return ProblemIdResponseDto.builder()
                .aiProblemChoiceId(aiProblemChoice.getAiProblemChoiceId())
                .aiProblemChoiceContent(aiProblemChoice.getAiProblemChoiceContent())
                .build();
    }



}

