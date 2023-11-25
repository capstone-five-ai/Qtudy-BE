package com.app.domain.summary.controller;

import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByFileRequestDto;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryByTextRequestDto;
import com.app.domain.summary.dto.SummaryFile.Response.AiGenerateSummaryByFileResponseDto;
import com.app.domain.summary.dto.SummaryFile.Response.AiGenerateSummaryByTextResponseDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.service.SummaryFileService;
import com.app.global.config.ENUM.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/summaryFile")
public class SummaryFileController {

    @Autowired
    SummaryFileService summaryFileService;

    @PostMapping("/generateSummaryFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryByTextResponseDto> AiGenerateSummaryFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateSummaryByTextRequestDto aiGenerateSummaryByTextRequestDto) {
        AiGeneratedSummary aiGeneratedSummary = summaryFileService.AiGenerateSummaryFileByText(token, aiGenerateSummaryByTextRequestDto);


        return ResponseEntity.ok(ConvertToSummaryTextResponse(aiGeneratedSummary));
    }

    @PostMapping(value = "/generateSummaryFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryByFileResponseDto> AiGenerateSummaryImageByImage(@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> imageFile, @ModelAttribute AiGenerateSummaryByFileRequestDto aiGenerateSummaryByFileRequestDto) {
        AiGeneratedSummary aiGeneratedSummary = summaryFileService.AiGenerateSummaryFileByFile(token,imageFile, aiGenerateSummaryByFileRequestDto, FileType.JPG); // pdf List 전체 다 추가


        return ResponseEntity.ok(ConvertToSummaryFileResponse(aiGeneratedSummary));
    }

    @PostMapping("/generateSummaryFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryByFileResponseDto> AiGenerateSummaryFileByPdf(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateSummaryByFileRequestDto aiGenerateSummaryByFileRequestDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        AiGeneratedSummary aiGeneratedSummary = summaryFileService.AiGenerateSummaryFileByFile(token, pdfFileList, aiGenerateSummaryByFileRequestDto, FileType.PDF); // List에 하나만 추가

        return ResponseEntity.ok(ConvertToSummaryFileResponse(aiGeneratedSummary));
    }






    public AiGenerateSummaryByTextResponseDto ConvertToSummaryTextResponse(AiGeneratedSummary aiGeneratedSummary){
        AiGenerateSummaryByTextResponseDto aiGenerateSummaryByTextResponseDto = AiGenerateSummaryByTextResponseDto.builder()
                .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
                .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
                .build();

        return aiGenerateSummaryByTextResponseDto;
    }

    public AiGenerateSummaryByFileResponseDto ConvertToSummaryFileResponse(AiGeneratedSummary aiGeneratedSummary){
        AiGenerateSummaryByFileResponseDto aiGenerateSummaryByFileResponseDto = AiGenerateSummaryByFileResponseDto.builder()
                .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
                .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
                .build();

        return aiGenerateSummaryByFileResponseDto;
    }

}
