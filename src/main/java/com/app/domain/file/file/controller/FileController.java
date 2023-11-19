package com.app.domain.file.file.controller;

import com.app.domain.file.file.dto.SearchFileDto;
import com.app.domain.file.file.dto.UpdateFileDto;
import com.app.domain.file.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/searchAiFileList") //사용자가 생성한 모든 파일이름 리스트 가져오기
    public ResponseEntity<List<String>> allFileList(@RequestHeader("Authorization") String token){
        List<String> fileList = fileService.allFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/searchFile") //사용자가 생성한 특정 파일 리스트 가져오기
    public ResponseEntity<List<String>> searchFileList(@RequestHeader("Authorization") String token,@Valid @RequestBody SearchFileDto searchFileDto){
        List<String> fileList = fileService.searchFileList(token, searchFileDto);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/updateFile") //사용자가 생성한 특정 파일 update
    public ResponseEntity updateFile(@RequestHeader("Authorization") String token,@Valid @RequestBody UpdateFileDto updateFileDto){
        fileService.updateFile(token,updateFileDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/downloadProblemFile") // 문제 파일 다운로드
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileDto searchFileDto) {
        byte[] fileContent = fileService.downloadFile(token,searchFileDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", searchFileDto.getFileName());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }




}
