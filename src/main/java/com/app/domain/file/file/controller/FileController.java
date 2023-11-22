package com.app.domain.file.file.controller;

import com.app.domain.file.file.dto.Response.FileListResponseDto;
import com.app.domain.file.file.dto.Request.SearchFileByFileIdRequestDto;
import com.app.domain.file.file.dto.Request.SearchFileByNameRequestDto;
import com.app.domain.file.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.file.dto.Response.SearchFileByFileIdResponseDto;
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
    public ResponseEntity<List<FileListResponseDto>> allFileList(@RequestHeader("Authorization") String token){
        List<FileListResponseDto> fileList = fileService.allFileList(token);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/searchFile") //사용자가 생성한 특정 파일 리스트 가져오기
    public ResponseEntity<List<FileListResponseDto>> searchFileList(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByNameRequestDto searchFileByNameRequestDto){
        List<FileListResponseDto> fileList = fileService.searchFileList(token, searchFileByNameRequestDto);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/updateFile") //사용자가 생성한 특정 파일 이름 update
    public ResponseEntity updateFile(@RequestHeader("Authorization") String token,@Valid @RequestBody UpdateFileRequestDto updateFileRequestDto){
        fileService.updateFile(token, updateFileRequestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/downloadProblemFile") // 문제 파일 다운로드
    public ResponseEntity<SearchFileByFileIdResponseDto> downloadFile(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByFileIdRequestDto searchFileByFileIdRequestDto) {
        String fileUrl = fileService.downloadFile(token, searchFileByFileIdRequestDto);

        return new ResponseEntity<>(new SearchFileByFileIdResponseDto(fileUrl), HttpStatus.OK);
    }




}
