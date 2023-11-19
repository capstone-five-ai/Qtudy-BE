package com.app.domain.file.file.service;


import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.file.dto.SearchFileDto;
import com.app.domain.file.file.dto.UpdateFileDto;
import com.app.domain.file.file.entity.Files;
import com.app.domain.file.file.repository.FilesRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private  S3Service s3Service;

    @Autowired
    private FilesRepository FilesRepository;

    public List<String> allFileList(String token){ //사용자가 생성한 모든 파일이름 리스트 가져오기

        List<String> FilesNameList = FilesRepository.findFileNameByMemberId(token); // 파일 이름 리스트 가져옴
        if(FilesNameList.isEmpty())
            return null;
        else
            return FilesNameList;
    }

    public List<String> searchFileList(String token, SearchFileDto searchFileDto){ //사용자가 생성한 특정 파일 리스트 가져오기
        String fileName = searchFileDto.getFileName();

        List<String> FilesNameList = FilesRepository.findFileNameByMemberIdAndFileNameContains(token,fileName);

        if(FilesNameList.isEmpty())
            return null;
        else
            return FilesNameList;
    }

    public void updateFile(String token, UpdateFileDto updateFileDto){
        String fileName = updateFileDto.getNewFileName();
        String newFileName = updateFileDto.getNewFileName();


        Optional<Files> file = FilesRepository.findFileNameByMemberIdAndFileName(token,fileName);

        file.ifPresent(entity ->{
            entity.setFileName(newFileName);
        });

        file.ifPresent(FilesRepository::save);
    }

    public byte[] downloadFile(String token, SearchFileDto searchFileDto){
        String fileName = searchFileDto.getFileName();

        Optional<String> key = FilesRepository.findFileKeyByMemberIdAndFileName(token,fileName);
        String fileKey = key.orElseThrow(() -> new BusinessException(ErrorCode.NOT_DOWNLOAD_FILE)); // KEY가 없으면 에러발생
        byte[] fileData = s3Service.downloadFile(fileKey); //KEY를 사용해 파일 Download

        return fileData;
    }




}
