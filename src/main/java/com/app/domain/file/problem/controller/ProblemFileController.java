package com.app.domain.file.problem.controller;

import com.app.domain.file.common.ENUM.FileType;
import com.app.domain.file.problem.dto.ProblemFile.Request.AiGenerateProblemByFileDto;
import com.app.domain.file.problem.dto.ProblemFile.Request.AiGenerateProblemByTextDto;
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

    @PostMapping("/generateProblemFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemByTextDto aiGenerateProblemByTextDto) {
        problemFileService.AiGenerateProblemFileByText(token, aiGenerateProblemByTextDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/generateProblemFileByImage") // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemImageByImage(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        problemFileService.AiGenerateProblemFileByFile(token, aiGenerateProblemByFileDto, FileType.JPG);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByPdf(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        problemFileService.AiGenerateProblemFileByFile(token, aiGenerateProblemByFileDto, FileType.PDF);

        return ResponseEntity.ok("Sucess");
    }




    @PostMapping("/deleteProblemFile") // 문제파일 삭제
    public ResponseEntity<String> deleteProblemFile(@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteProblemFileDto deleteProblemFileDto){
        problemFileService.DeleteProblemFile(token,deleteProblemFileDto);

        return ResponseEntity.ok("Sucess");
    }


}

