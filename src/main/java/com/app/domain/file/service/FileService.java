package com.app.domain.file.service;


import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.DuplicateFileNameRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.DuplicateFileNameResponseDto;
import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.file.entity.File;
import com.app.domain.file.repository.FileRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.config.ENUM.DType;
import com.app.global.config.ENUM.PdfType;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private  S3Service s3Service;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MemberService memberService;


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

    public void updateFile( int fileId, UpdateFileRequestDto updateFileRequestDto){
        String newFileName = updateFileRequestDto.getNewFileName();


        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));

        file.setFileName(newFileName);
        fileRepository.save(file);
    }

    public String downloadFile( int fileId, DownloadPdfRequestDto downloadPdfRequestDto){
        PdfType pdfType = downloadPdfRequestDto.getPdfType();
        String UrlKey = null;


        Optional<File> optionalFiles = fileRepository.findByFileId(fileId);
        if(optionalFiles.isPresent()){
            File file = optionalFiles.get();
            switch(pdfType){
                case PROBLEM: //문제PDF
                    if(file.getDtype() != DType.PROBLEM)
                        new BusinessException(ErrorCode.INVALID_DTYPE);
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_PROBLEM.pdf");
                    break;
                case ANSWER: //정답PDF
                    if(file.getDtype() != DType.PROBLEM)
                        new BusinessException(ErrorCode.INVALID_DTYPE);
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_ANSWER.pdf");
                    break;
                case SUMMARY: //요점정리PDF
                    if(file.getDtype() != DType.SUMMARY)
                        new BusinessException(ErrorCode.INVALID_DTYPE);
                    UrlKey = ConvertUrlByKey(file.getFileId() + "_SUMMARY.pdf");
                    break;
            }
        }else{
            throw new BusinessException(ErrorCode.NOT_EXIST_FILE); //파일이 존재하지 않은경우
        }

        return UrlKey;
    }

    public String ConvertUrlByKey(String fileKey){
        return "https://q-study-bucket.s3.amazonaws.com/"+fileKey;
    }


    @Transactional
    public void DeleteProblemFile(int fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));

        fileRepository.delete(file);
    }

    public DuplicateFileNameResponseDto duplicateFileName(DuplicateFileNameRequestDto duplicateFileNameRequestDto){
        if(fileRepository.findByFileName(duplicateFileNameRequestDto.getFileName()).isPresent())
            return new DuplicateFileNameResponseDto(true); // 중복
        return new DuplicateFileNameResponseDto(false); // 중복 X
    }

}
