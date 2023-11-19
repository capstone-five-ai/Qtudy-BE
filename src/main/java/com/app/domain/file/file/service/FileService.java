package com.app.domain.file.file.service;


import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.file.dto.FileListResponseDto;
import com.app.domain.file.file.dto.SearchFileByFileIdDto;
import com.app.domain.file.file.dto.SearchFileByNameDto;
import com.app.domain.file.file.dto.UpdateFileDto;
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

    public List<FileListResponseDto> searchFileList(String token, SearchFileByNameDto searchFileByNameDto){ //사용자가 생성한 특정 파일 리스트 가져오기
        String fileName = searchFileByNameDto.getFileName();

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

    public void updateFile(String token, UpdateFileDto updateFileDto){
        int fileId = updateFileDto.getFileId();
        String newFileName = updateFileDto.getNewFileName();


        Optional<Files> file = FilesRepository.findByFileId(fileId);

        file.ifPresent(entity ->{
            entity.setFileName(newFileName);
        });

        file.ifPresent(FilesRepository::save);
    }

    public byte[] downloadFile(String token, SearchFileByFileIdDto searchFileByFileIdDto){
        int fileId = searchFileByFileIdDto.getFileId();
        String fileName = searchFileByFileIdDto.getFileName();

        Optional<String> key = FilesRepository.findFileKeyByFileId(fileId);
        String fileKey = key.orElseThrow(() -> new BusinessException(ErrorCode.NOT_DOWNLOAD_FILE)); // KEY가 없으면 에러발생
        byte[] fileData = s3Service.downloadFile(fileKey); //KEY를 사용해 파일 Download

        return fileData;
    }




}
