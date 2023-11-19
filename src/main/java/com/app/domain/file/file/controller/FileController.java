package com.app.domain.file.file.controller;

import com.app.domain.file.file.dto.FileListResponseDto;
import com.app.domain.file.file.dto.SearchFileByFileIdDto;
import com.app.domain.file.file.dto.SearchFileByNameDto;
import com.app.domain.file.file.dto.UpdateFileDto;
import com.app.domain.file.file.service.FileService;
import com.app.domain.file.problem.dto.ProblemFile.AiGenerateProblemDto;
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
    public ResponseEntity<List<FileListResponseDto>> searchFileList(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByNameDto searchFileByNameDto){
        List<FileListResponseDto> fileList = fileService.searchFileList(token, searchFileByNameDto);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    @PostMapping("/updateFile") //사용자가 생성한 특정 파일 이름 update
    public ResponseEntity updateFile(@RequestHeader("Authorization") String token,@Valid @RequestBody UpdateFileDto updateFileDto){
        fileService.updateFile(token,updateFileDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/downloadProblemFile") // 문제 파일 다운로드
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("Authorization") String token, @Valid @RequestBody SearchFileByFileIdDto searchFileByFileIdDto) {
        byte[] fileContent = fileService.downloadFile(token, searchFileByFileIdDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", searchFileByFileIdDto.getFileName());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }




}
