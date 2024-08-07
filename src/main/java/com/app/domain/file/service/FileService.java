package com.app.domain.file.service;


import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.DuplicateFileNameRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.DuplicateFileNameResponseDto;
import com.app.domain.file.entity.File;
import com.app.domain.file.repository.FileRepository;
import com.app.domain.member.service.MemberService;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.domain.problem.aigeneratedproblem.repository.ProblemFileRepository;
import com.app.domain.summary.aigeneratedsummary.entity.SummaryFile;
import com.app.domain.summary.aigeneratedsummary.repository.SummaryFileRepository;
import com.app.global.config.ENUM.DType;
import com.app.global.config.ENUM.PdfType;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private  S3Service s3Service;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ProblemFileRepository problemFileRepository;

    @Autowired
    private SummaryFileRepository summaryFileRepository;

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

    public void updateFile( Long fileId, UpdateFileRequestDto updateFileRequestDto){
        String newFileName = updateFileRequestDto.getNewFileName();


        File file = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));

        file.setFileName(newFileName);
        fileRepository.save(file);
    }

    public String downloadFile( Long fileId, DownloadPdfRequestDto downloadPdfRequestDto){
        PdfType pdfType = downloadPdfRequestDto.getPdfType();
        String UrlKey = null;

        switch(pdfType){
            case PROBLEM: //문제PDF
                if (problemFileRepository.findByFileId(fileId).isEmpty()) {
                    throw new BusinessException(ErrorCode.NOT_EXIST_FILE);
                }
                UrlKey = ConvertUrlByKey(fileId + "_PROBLEM.pdf");
                break;
            case ANSWER: //정답PDF
                if (problemFileRepository.findByFileId(fileId).isEmpty()) {
                    throw new BusinessException(ErrorCode.NOT_EXIST_FILE);
                }
                UrlKey = ConvertUrlByKey(fileId + "_ANSWER.pdf");
                break;
            case SUMMARY: //요점정리PDF
                if (summaryFileRepository.findByFileId(fileId).isEmpty()) {
                    throw new BusinessException(ErrorCode.NOT_EXIST_FILE);
                }
                UrlKey = ConvertUrlByKey(fileId + "_SUMMARY.pdf");
                break;
        }

        return UrlKey;
    }

    public String ConvertUrlByKey(String fileKey){
        return "https://q-study-bucket.s3.amazonaws.com/"+fileKey;
    }


    @Transactional
    public void DeleteProblemFile(long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));

        fileRepository.delete(file);
    }

    public DuplicateFileNameResponseDto duplicateFileName(DuplicateFileNameRequestDto duplicateFileNameRequestDto){
        Optional<File> duplicatedFile = fileRepository.findByFileNameAndDtype(
                duplicateFileNameRequestDto.getFileName(),
                duplicateFileNameRequestDto.getType()
        );

        if (duplicatedFile.isPresent()) {
            return new DuplicateFileNameResponseDto(true);
        }
        return new DuplicateFileNameResponseDto(false);
    }
}
