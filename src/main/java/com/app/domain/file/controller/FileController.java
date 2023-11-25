package com.app.domain.file.controller;

import com.app.domain.file.dto.Request.DeleteProblemFileDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.global.config.ENUM.PdfType;
import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.file.dto.Request.SearchFileByFileIdRequestDto;
import com.app.domain.file.dto.Response.SearchFileByFileIdResponseDto;
import com.app.domain.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/searchAiProblemFileList") //사용자가 생성한 모든 문제 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<List<FileListResponseDto>> allAiProblemFileList(@RequestHeader("Authorization") String token){
        List<FileListResponseDto> fileList = fileService.allAiProblemFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/searchAiSummaryFileList") //사용자가 생성한 모든 요점정리 리스트 가져오기 (생성 히스토리)
    public ResponseEntity<List<FileListResponseDto>> allAiSummaryFileList(@RequestHeader("Authorization") String token){
        List<FileListResponseDto> fileList = fileService.allAiSummaryFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }


    /*@PostMapping("/searchFile") //사용자가 생성한 특정 파일 리스트 가져오기
    public ResponseEntity<List<FileListResponseDto>> searchFileList(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByNameRequestDto searchFileByNameRequestDto){
        List<FileListResponseDto> fileList = fileService.searchFileList(token, searchFileByNameRequestDto);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }*/

    @PostMapping("/updateFile") //사용자가 생성한 파일 이름 update
    public ResponseEntity<String> updateFile(@RequestHeader("Authorization") String token,@Valid @RequestBody UpdateFileRequestDto updateFileRequestDto){
        fileService.updateFile(token, updateFileRequestDto);

        return new ResponseEntity<>("Sucess",HttpStatus.OK);
    }

    @PostMapping("/downloadProblemPdf") // 문제 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadProblemPdf(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByFileIdRequestDto searchFileByFileIdRequestDto) {
        String fileUrl = fileService.downloadFile(token, searchFileByFileIdRequestDto, PdfType.PROBLEM);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @PostMapping("/downloadAnswerPdf") // 문제정답 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadAnswerPdf(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByFileIdRequestDto searchFileByFileIdRequestDto) {
        String fileUrl = fileService.downloadFile(token, searchFileByFileIdRequestDto, PdfType.ANSWER);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @PostMapping("/downloadSummaryPdf") // 요점정리 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadSummaryPdf(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByFileIdRequestDto searchFileByFileIdRequestDto) {
        String fileUrl = fileService.downloadFile(token, searchFileByFileIdRequestDto,PdfType.SUMMARY);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @PostMapping("/deleteProblemFile") // 문제파일 삭제
    public ResponseEntity<String> deleteProblemFile(@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteProblemFileDto deleteProblemFileDto){
        fileService.DeleteProblemFile(token,deleteProblemFileDto);

        return ResponseEntity.ok("Sucess");
    }




}
