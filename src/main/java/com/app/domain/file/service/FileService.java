package com.app.domain.file.service;


import com.app.domain.file.dto.Request.DeleteProblemFileDto;
import com.app.domain.file.dto.Request.SearchFileByFileIdRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.file.entity.Files;
import com.app.global.config.ENUM.DType;
import com.app.global.config.ENUM.PdfType;
import com.app.global.config.S3.S3Service;
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
    private com.app.domain.file.repository.FilesRepository FilesRepository;

    public List<FileListResponseDto> allAiProblemFileList(String token){ //사용자가 생성한 모든 문제파일 리스트 가져오기

        List<Files> fileList = FilesRepository.findByMemberIdAndDtype(token, DType.PROBLEM); // 문제파일 리스트 가져옴
       List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getDtype(),
                        file.getCreateTime()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }

    public List<FileListResponseDto> allAiSummaryFileList(String token){ //사용자가 생성한 모든 요점정리파일 리스트 가져오기

        List<Files> fileList = FilesRepository.findByMemberIdAndDtype(token,DType.SUMMARY); // 요점정리파일 이름 가져오기

        List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getDtype(),
                        file.getCreateTime()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }

    /*public List<FileListResponseDto> searchFileList(String token, SearchFileByNameRequestDto searchFileByNameRequestDto){ //사용자가 생성한 특정 파일 리스트 가져오기
        String fileName = searchFileByNameRequestDto.getFileName();

        List<Files> fileList = FilesRepository.findByMemberIdAndFileNameContains(token,fileName);

        List<FileListResponseDto> fileListResponseDtoList = fileList.stream()
                .map(file -> new FileListResponseDto(
                        file.getFileId(),
                        file.getFileName(),
                        file.getDtype(),
                        file.getFileDate()
                ))
                .collect(Collectors.toList());
        return fileListResponseDtoList;
    }*/

    public void updateFile(String token, UpdateFileRequestDto updateFileRequestDto){
        int fileId = updateFileRequestDto.getFileId();
        String newFileName = updateFileRequestDto.getNewFileName();


        Optional<Files> file = FilesRepository.findByFileId(fileId);

        file.ifPresent(entity ->{
            entity.setFileName(newFileName);
        });

        file.ifPresent(FilesRepository::save);
    }

    public String downloadFile(String token, SearchFileByFileIdRequestDto searchFileByFileIdRequestDto , PdfType pdfType){
        int fileId = searchFileByFileIdRequestDto.getFileId();
        String fileName = searchFileByFileIdRequestDto.getFileName();
        String UrlKey = null;

        Optional<Files> optionalFiles = FilesRepository.findByFileId(fileId);
        if(optionalFiles.isPresent()){
            Files files = optionalFiles.get();
            switch(pdfType){
                case PROBLEM: //문제PDF
                    UrlKey = ConvertUrlByKey(files.getFileKey()+"_PROBLEM");
                    break;
                case ANSWER: //정답PDF
                    UrlKey = ConvertUrlByKey(files.getFileKey()+"_ANSWER");
                    break;
                case SUMMARY: //요점정리PDF
                    UrlKey = ConvertUrlByKey(files.getFileKey()+"_SUMMARY");
                    break;
            }
        }else{
            //추후 에러처리 할 예정
        }
        return UrlKey;
    }

    public String ConvertUrlByKey(String fileKey){
        return "https://q-study-bucket.s3.amazonaws.com/"+fileKey;
    }


    public void DeleteProblemFile(String token, DeleteProblemFileDto deleteProblemFileDto) {
        int fileId = deleteProblemFileDto.getFileId();

        Optional<Files> optionalFile = FilesRepository.findById(fileId);

        if (optionalFile.isPresent()) {
            Files file = optionalFile.get();
            FilesRepository.delete(file);
        } else {
            // 추후 에러 처리할 예정...
        }
    }
}
