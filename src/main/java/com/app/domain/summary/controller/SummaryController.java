package com.app.domain.summary.controller;

import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByFileDto;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByTextDto;
import com.app.domain.summary.service.SummaryFileService;
import com.app.global.config.ENUM.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    SummaryFileService summaryFileService;

    @PostMapping("/generateProblemFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateSummaryByTextDto aiGenerateSummaryByTextDto) {
        summaryFileService.AiGenerateSummaryFileByText(token, aiGenerateSummaryByTextDto);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping(value = "/generateProblemFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemImageByImage(@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> imageFile, @ModelAttribute AiGenerateSummaryByFileDto aiGenerateSummaryByFileDto) {
        summaryFileService.AiGenerateSummaryFileByFile(token,imageFile, aiGenerateSummaryByFileDto, FileType.JPG); // pdf List 전체 다 추가

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<String> AiGenerateProblemFileByPdf(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateSummaryByFileDto aiGenerateSummaryByFileDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        summaryFileService.AiGenerateSummaryFileByFile(token, pdfFileList,aiGenerateSummaryByFileDto, FileType.PDF); // List에 하나만 추가

        return ResponseEntity.ok("Sucess");
    }



}
