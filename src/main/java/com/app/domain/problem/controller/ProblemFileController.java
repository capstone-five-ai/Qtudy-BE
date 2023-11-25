package com.app.domain.problem.controller;

import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByFileDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByTextDto;
import com.app.global.config.ENUM.FileType;
import com.app.domain.problem.service.ProblemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/problemFile")
public class ProblemFileController { // Controller 추후 분할 예정
    @Autowired
    ProblemFileService problemFileService;

    @PostMapping("/generateProblemFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemByTextDto aiGenerateProblemByTextDto) {
        problemFileService.AiGenerateProblemFileByText(token, aiGenerateProblemByTextDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping(value = "/generateProblemFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemImageByImage(@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> imageFile, @ModelAttribute AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        problemFileService.AiGenerateProblemFileByFile(token,imageFile, aiGenerateProblemByFileDto, FileType.JPG); // pdf List 전체 다 추가

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByPdf(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        problemFileService.AiGenerateProblemFileByFile(token, pdfFileList,aiGenerateProblemByFileDto, FileType.PDF); // List에 하나만 추가

        return ResponseEntity.ok("Sucess");
    }

}

