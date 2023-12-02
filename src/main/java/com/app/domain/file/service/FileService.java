package com.app.domain.file.service;


import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.file.entity.File;
import com.app.domain.file.repository.FileRepository;
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
    private FileRepository FileRepository;


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

    public void updateFile(String token,int fileId, UpdateFileRequestDto updateFileRequestDto){
        String newFileName = updateFileRequestDto.getNewFileName();


        Optional<File> file = FileRepository.findByFileId(fileId);

        file.ifPresent(entity ->{
            entity.setFileName(newFileName);
        });

        file.ifPresent(FileRepository::save);
    }

    public String downloadFile(String token, int fileId, DownloadPdfRequestDto downloadPdfRequestDto){
        PdfType pdfType = downloadPdfRequestDto.getPdfType();
        String UrlKey = null;

        Optional<File> optionalFiles = FileRepository.findByFileId(fileId);
        if(optionalFiles.isPresent()){
            File file = optionalFiles.get();
            switch(pdfType){
                case PROBLEM: //문제PDF
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_PROBLEM.pdf");
                    break;
                case ANSWER: //정답PDF
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_ANSWER.pdf");
                    break;
                case SUMMARY: //요점정리PDF
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_SUMMARY.pdf");
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


    public void DeleteProblemFile(String token, int fileId) {

        Optional<File> optionalFile = FileRepository.findById(fileId);

        if (optionalFile.isPresent()) {
            File file = optionalFile.get();
            FileRepository.delete(file);
        } else {
            // 추후 에러 처리할 예정...
        }
    }
}
