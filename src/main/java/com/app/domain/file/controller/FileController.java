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




    @PatchMapping("/updateFile/{fileId}") //사용자가 생성한 파일 이름 update
    public ResponseEntity<String> updateFile(@RequestHeader("Authorization") String token,@PathVariable int fileId,@Valid @RequestBody UpdateFileRequestDto updateFileRequestDto){
        fileService.updateFile(token,fileId, updateFileRequestDto);

        return new ResponseEntity<>("Sucess",HttpStatus.OK);
    }

    @GetMapping("/downloadProblemPdf/{fileId}") // 문제 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadProblemPdf(@RequestHeader("Authorization") String token,@PathVariable int fileId) {
        String fileUrl = fileService.downloadFile(token,fileId, PdfType.PROBLEM);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @GetMapping("/downloadAnswerPdf/{fileId}") // 문제정답 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadAnswerPdf(@RequestHeader("Authorization") String token,@PathVariable int fileId) {
        String fileUrl = fileService.downloadFile(token,fileId , PdfType.ANSWER);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @GetMapping("/downloadSummaryPdf/{fileId}") // 요점정리 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadSummaryPdf(@RequestHeader("Authorization") String token,@PathVariable int fileId) {
        String fileUrl = fileService.downloadFile(token,fileId,PdfType.SUMMARY);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile/{fileId}") // 문제파일 삭제
    public ResponseEntity<String> deleteProblemFile(@RequestHeader("Authorization") String token,@PathVariable int fileId){
        fileService.DeleteProblemFile(token,fileId);

        return ResponseEntity.ok("Sucess");
    }




}
