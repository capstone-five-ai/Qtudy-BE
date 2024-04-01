package com.app.domain.summary.controller;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.summary.dto.SummaryFile.Request.AiGenerateSummaryDto;
import com.app.domain.summary.dto.SummaryFile.Response.AiGenerateSummaryResponseDto;
import com.app.domain.summary.service.SummaryFileService;
import com.app.global.config.ENUM.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/summaryFile")
public class SummaryFileController {

    @Autowired
    SummaryFileService summaryFileService;

    @PostMapping("/generateSummaryFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryFileByText(HttpServletRequest httpServletRequest, @Valid @RequestBody AiGenerateSummaryDto aiGenerateSummaryDto) {
        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = summaryFileService.AiGenerateSummaryFileByText(httpServletRequest, aiGenerateSummaryDto);

        return ResponseEntity.ok(aiGenerateSummaryResponseDto);
    }

    @PostMapping(value = "/generateSummaryFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryImageByImage(HttpServletRequest httpServletRequest, @RequestParam("file") List<MultipartFile> imageFile, @ModelAttribute AiGenerateSummaryDto aiGenerateSummaryDto) {
        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = summaryFileService.AiGenerateSummaryFileByFile(httpServletRequest,imageFile, aiGenerateSummaryDto, FileType.JPG); // pdf List 전체 다 추가

        return ResponseEntity.ok(aiGenerateSummaryResponseDto);
    }

    @PostMapping("/generateSummaryFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryFileByPdf(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateSummaryDto aiGenerateSummaryDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);

        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = summaryFileService.AiGenerateSummaryFileByFile(httpServletRequest, pdfFileList, aiGenerateSummaryDto, FileType.PDF); // List에 하나만 추가

        return ResponseEntity.ok(aiGenerateSummaryResponseDto);
    }

    @GetMapping("/searchAiSummaryFileList/{pageNumber}") //사용자가 생성한 모든 요점정리 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<Page<FileListResponseDto>> allAiSummaryFileList(HttpServletRequest httpServletRequest,@PathVariable int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber-1,9);

        Page<FileListResponseDto> fileList = summaryFileService.allAiSummaryFileList(pageable, httpServletRequest);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }



}
