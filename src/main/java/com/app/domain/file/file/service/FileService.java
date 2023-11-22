package com.app.domain.file.file.service;


import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.file.dto.Response.FileListResponseDto;
import com.app.domain.file.file.dto.Request.SearchFileByFileIdRequestDto;
import com.app.domain.file.file.dto.Request.SearchFileByNameRequestDto;
import com.app.domain.file.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.file.entity.Files;
import com.app.domain.file.file.repository.FilesRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private  S3Service s3Service;

    @Autowired
    private FilesRepository FilesRepository;

    public List<FileListResponseDto> allFileList(String token){ //사용자가 생성한 모든 파일이름 리스트 가져오기

        List<Files> fileList = FilesRepository.findByMemberId(token); // 파일 이름 리스트 가져옴

        List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getFileDate()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }

    public List<FileListResponseDto> searchFileList(String token, SearchFileByNameRequestDto searchFileByNameRequestDto){ //사용자가 생성한 특정 파일 리스트 가져오기
        String fileName = searchFileByNameRequestDto.getFileName();

        List<Files> fileList = FilesRepository.findByMemberIdAndFileNameContains(token,fileName);

        List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getFileDate()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }

    public void updateFile(String token, UpdateFileRequestDto updateFileRequestDto){
        int fileId = updateFileRequestDto.getFileId();
        String newFileName = updateFileRequestDto.getNewFileName();


        Optional<Files> file = FilesRepository.findByFileId(fileId);

        file.ifPresent(entity ->{
            entity.setFileName(newFileName);
        });

        file.ifPresent(FilesRepository::save);
    }

    public String downloadFile(String token, SearchFileByFileIdRequestDto searchFileByFileIdRequestDto){
        int fileId = searchFileByFileIdRequestDto.getFileId();
        String fileName = searchFileByFileIdRequestDto.getFileName();

        Optional<String> key = FilesRepository.findFileKeyByFileId(fileId);
        String fileKey = key.orElseThrow(() -> new BusinessException(ErrorCode.NOT_DOWNLOAD_FILE)); // KEY가 없으면 에러발생
        String fileUrl=ConvertUrlByKey(fileKey);

        return fileUrl;
    }

    public String ConvertUrlByKey(String fileKey){
        return "https://q-study-bucket.s3.amazonaws.com/"+fileKey;
    }




}
