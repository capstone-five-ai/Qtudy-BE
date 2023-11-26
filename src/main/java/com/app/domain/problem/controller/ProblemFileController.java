package com.app.domain.problem.controller;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.problem.dto.Problem.Response.FileIdResponseDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByFileDto;
import com.app.domain.problem.dto.ProblemFile.Request.AiGenerateProblemByTextDto;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateMultipleProblemResponseDto;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateProblemResponseDto;
import com.app.domain.problem.dto.ProblemFile.Response.GenerateSubjectiveProblemResponseDto;
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
    public ResponseEntity<List<? extends GenerateProblemResponseDto>> AiGenerateProblemFileByText(@RequestHeader("Authorization") String token, @Valid @RequestBody AiGenerateProblemByTextDto aiGenerateProblemByTextDto) {
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByText(token, aiGenerateProblemByTextDto);

        List<GenerateProblemResponseDto> responseDto = new ArrayList<>();
        switch(aiGenerateProblemByTextDto.getType()){
            case MULTIPLE:
                responseDto = problems.stream()
                        .map(this::convertToMultiple)
                        .collect(Collectors.toList());
                break;
            case SUBJECTIVE:
                responseDto = problems.stream()
                        .map(this::convertToSubjective)
                        .collect(Collectors.toList());
        }

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/generateProblemFileByImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // 이미지(png,jpg)기반 문제파일 생성
    public ResponseEntity<List<GenerateProblemResponseDto>> AiGenerateProblemImageByImage(@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> imageFile, @ModelAttribute AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByFile(token,imageFile, aiGenerateProblemByFileDto, FileType.JPG); // pdf List 전체 다 추가

        List<GenerateProblemResponseDto> responseDto = new ArrayList<>();
        switch(aiGenerateProblemByFileDto.getType()){
            case MULTIPLE:
                responseDto = problems.stream()
                        .map(this::convertToMultiple)
                        .collect(Collectors.toList());
                break;
            case SUBJECTIVE:
                responseDto = problems.stream()
                        .map(this::convertToSubjective)
                        .collect(Collectors.toList());
        }

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/generateProblemFileByPdf") // PDF 기반 문제파일 생성
    public ResponseEntity<List<GenerateProblemResponseDto>> AiGenerateProblemFileByPdf(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile pdfFile, @ModelAttribute AiGenerateProblemByFileDto aiGenerateProblemByFileDto) {
        List<MultipartFile> pdfFileList = new ArrayList<>();
        pdfFileList.add(pdfFile);
        List<AiGeneratedProblem> problems = problemFileService.AiGenerateProblemFileByFile(token, pdfFileList,aiGenerateProblemByFileDto, FileType.PDF); // List에 하나만 추가

        List<GenerateProblemResponseDto> responseDto = new ArrayList<>();
        switch(aiGenerateProblemByFileDto.getType()){
            case MULTIPLE:
                responseDto = problems.stream()
                        .map(this::convertToMultiple)
                        .collect(Collectors.toList());
                break;
            case SUBJECTIVE:
                responseDto = problems.stream()
                        .map(this::convertToSubjective)
                        .collect(Collectors.toList());
        }

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/searchAiProblemFileList") //사용자가 생성한 모든 문제 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<List<FileListResponseDto>> allAiProblemFileList(@RequestHeader("Authorization") String token){
        List<FileListResponseDto> fileList = problemFileService.allAiProblemFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }




    private GenerateMultipleProblemResponseDto convertToMultiple(AiGeneratedProblem aiGeneratedProblem) {
        return GenerateMultipleProblemResponseDto.multipleBuilder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemChoices(aiGeneratedProblem.getProblemChoices())
                .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .createTime(aiGeneratedProblem.getCreateTime())
                .updateTime(aiGeneratedProblem.getUpdateTime())
                .build();
    }

    private GenerateSubjectiveProblemResponseDto convertToSubjective(AiGeneratedProblem aiGeneratedProblem) {
        return GenerateSubjectiveProblemResponseDto.subjectiveBuilder()
                .aiGeneratedProblemId(aiGeneratedProblem.getAiGeneratedProblemId())
                .problemName(aiGeneratedProblem.getProblemName())
                .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                .createTime(aiGeneratedProblem.getCreateTime())
                .updateTime(aiGeneratedProblem.getUpdateTime())
                .build();
    }

}

