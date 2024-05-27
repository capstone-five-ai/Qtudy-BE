package com.app.domain.file.controller;

import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.DuplicateFileNameRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.DuplicateFileNameResponseDto;
import com.app.global.config.ENUM.PdfType;
import com.app.domain.file.dto.Response.DownloadPdfResponseDto;
import com.app.domain.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    FileService fileService;




    @PatchMapping("/updateFile/{fileId}") //사용자가 생성한 파일 이름 update
    public ResponseEntity<String> updateFile(@PathVariable int fileId, @Valid @RequestBody UpdateFileRequestDto updateFileRequestDto){
        fileService.updateFile(fileId, updateFileRequestDto);

        return new ResponseEntity<>("Sucess",HttpStatus.OK);
    }

    @PostMapping("/downloadPdf/{fileId}") // 문제 다운로드
    public ResponseEntity<DownloadPdfResponseDto> downloadProblemPdf( @PathVariable int fileId, @RequestBody DownloadPdfRequestDto downloadPdfRequestDto) {
        String fileUrl = fileService.downloadFile(fileId, downloadPdfRequestDto);

        return new ResponseEntity<>(new DownloadPdfResponseDto(fileUrl), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile/{fileId}") // 문제파일 삭제
    public ResponseEntity<String> deleteProblemFile(@PathVariable int fileId){
        fileService.DeleteProblemFile(fileId);

        return ResponseEntity.ok("Sucess");
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<DuplicateFileNameResponseDto> duplicateFileName (@RequestBody DuplicateFileNameRequestDto duplicateFileNameRequestDto){
        return new ResponseEntity<>(fileService.duplicateFileName(duplicateFileNameRequestDto), HttpStatus.OK);
    }




}
