package com.app.domain.problem.aigeneratedproblem.controller;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.problem.aigeneratedproblem.doc.ProblemFileApi;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemResponseDto;
import com.app.global.config.ENUM.FileType;
import com.app.domain.problem.aigeneratedproblem.service.ProblemFileService;
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
@RequestMapping("/api/problemFile")
public class ProblemFileController implements ProblemFileApi { // Controller 추후 분할 예정
    @Autowired
    ProblemFileService problemFileService;

    @PostMapping("/generateProblemFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemFileByText(HttpServletRequest httpServletRequest, @Valid @RequestBody AiGenerateProblemDto aiGenerateProblemDto) {
        AiGeneratedProblemResponseDto aiGeneratedProblemResponseDto =  problemFileService.AiGenerateProblemFileByText(httpServletRequest, aiGenerateProblemDto);

        return ResponseEntity.ok(aiGeneratedProblemResponseDto);
    }

    @PostMapping(value = "/generateProblemFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemImageByImage(HttpServletRequest httpServletRequest, @RequestParam("file") List<MultipartFile> file, @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto) {
        AiGeneratedProblemResponseDto aiGeneratedProblemResponseDto = problemFileService.AiGenerateProblemFileByFile(httpServletRequest,file, aiGenerateProblemDto, FileType.JPG); // pdf List 전체 다 추가


        return ResponseEntity.ok(aiGeneratedProblemResponseDto);
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemFileByPdf(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        
        AiGeneratedProblemResponseDto aiGeneratedProblemResponseDto = problemFileService.AiGenerateProblemFileByFile(httpServletRequest, pdfFileList,aiGenerateProblemDto, FileType.PDF); // List에 하나만 추가


        return ResponseEntity.ok(aiGeneratedProblemResponseDto);
    }


    @GetMapping("/searchAiProblemFileList/{pageNumber}") //사용자가 생성한 모든 문제 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<Page<FileListResponseDto>> allAiProblemFileList(HttpServletRequest httpServletRequest, @PathVariable int pageNumber){

        Pageable pageable = PageRequest.of(pageNumber-1,9);

        Page<FileListResponseDto> fileList = problemFileService.allAiProblemFileList(pageable,httpServletRequest);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }
}

