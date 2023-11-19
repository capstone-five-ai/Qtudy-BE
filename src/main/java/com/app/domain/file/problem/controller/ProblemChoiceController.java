package com.app.domain.file.problem.controller;

import com.app.domain.file.problem.dto.ProblemChoice.AddChoiceDto;
import com.app.domain.file.problem.dto.ProblemChoice.DeleteChoiceDto;
import com.app.domain.file.problem.dto.ProblemChoice.ProblemIdDto;
import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.service.ProblemChoiceService;
import com.app.domain.file.problem.service.ProblemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemChoiceController { // Controller 추후 분할 예정
    @Autowired
    ProblemChoiceService problemChoiceService;

    @PostMapping("/getProblemChoices") // 문제의 보기리스트 가져옴
    public ResponseEntity<List<AiProblemChoice>> GetProblemChoices(@RequestHeader("Authorization") String token, @Valid @RequestBody ProblemIdDto problemIdDto) {
        List<AiProblemChoice> choices = problemChoiceService.GetProblemChoices(token, problemIdDto);

        return ResponseEntity.ok(choices);
    }

    @PostMapping("addProblemChoice") // 문제의 보기 추가
        public ResponseEntity<String> AddProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody AddChoiceDto addChoiceDto) {
        String response = problemChoiceService.AddProblemChoice(token,addChoiceDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("deleteProblemChoice") // 문제의 보기 제거
    public ResponseEntity<String> DeleteProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteChoiceDto deleteChoiceDto) {
        String response = problemChoiceService.DeleteProblemChoice(token,deleteChoiceDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("updateProblemChoice") // 문제의 보기 제거
    public ResponseEntity<String> UpdateProblemChoices (@RequestHeader("Authorization") String token, @Valid @RequestBody AddChoiceDto addChoiceDto) {
        String response = problemChoiceService.AddProblemChoice(token,addChoiceDto);

        return ResponseEntity.ok(response);
    }



}

