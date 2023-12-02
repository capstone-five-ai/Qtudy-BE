package com.app.domain.problem.controller;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByFileDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.problem.dto.ProblemFile.Response.AiGeneratedProblemResponseDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.global.config.ENUM.FileType;
import com.app.domain.problem.service.ProblemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problemFile")
public class ProblemFileController { // Controller 추후 분할 예정
    @Autowired
    ProblemFileService problemFileService;

    @PostMapping("/generateProblemFileByText") // 텍스트기반 문제파일 생성
    public ResponseEntity<List<AiGeneratedProblemResponseDto>> AiGenerateProblemFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemDto aiGenerateProblemDto) {
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByText(token, aiGenerateProblemDto);

        List<AiGeneratedProblemResponseDto> responseDtos = problems.stream()
                .map(AiGeneratedProblemResponseDto::ConvertToProblem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping(value = "/generateProblemFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<List<AiGeneratedProblemResponseDto>> AiGenerateProblemImageByImage(@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> file, @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto) {
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByFile(token,file, aiGenerateProblemDto, FileType.JPG); // pdf List 전체 다 추가

        List<AiGeneratedProblemResponseDto> responseDtos = problems.stream()
                .map(AiGeneratedProblemResponseDto::ConvertToProblem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<List<AiGeneratedProblemResponseDto>> AiGenerateProblemFileByPdf(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByFile(token, pdfFileList,aiGenerateProblemDto, FileType.PDF); // List에 하나만 추가

        List<AiGeneratedProblemResponseDto> responseDtos = problems.stream()
                .map(AiGeneratedProblemResponseDto::ConvertToProblem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }


    @GetMapping("/searchAiProblemFileList") //사용자가 생성한 모든 문제 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<List<FileListResponseDto>> allAiProblemFileList(@RequestHeader("Authorization") String token){
        List<FileListResponseDto> fileList = problemFileService.allAiProblemFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }
}

