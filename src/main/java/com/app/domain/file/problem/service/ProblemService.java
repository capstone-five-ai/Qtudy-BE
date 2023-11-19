package com.app.domain.file.problem.service;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.Problem.FileNameDto;
import com.app.domain.file.problem.dto.ProblemChoice.AddChoiceDto;
import com.app.domain.file.problem.dto.ProblemChoice.DeleteChoiceDto;
import com.app.domain.file.problem.dto.ProblemChoice.ProblemIdDto;
import com.app.domain.file.problem.dto.ProblemChoice.UpdateChoiceDto;
import com.app.domain.file.problem.dto.ProblemFile.AiGenerateProblemDto;
import com.app.domain.file.problem.dto.ProblemFile.AiGenerateProblemResponseDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.AiProblemChoice;
import com.app.domain.file.problem.entity.ProblemFiles;
import com.app.domain.file.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.file.problem.repository.AiProblemChoiceRepository;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import com.app.domain.file.problem.value.S3FileInformation;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFilesRepository problemFilesRepository;
    @Autowired
    private AiGeneratedProblemsRepository aiGeneratedProblemsRepository;
    @Autowired
    private AiProblemChoiceRepository aiProblemChoiceRepository;
    @Autowired
    private S3Service s3Service;



    public List<AiGeneratedProblems> GetFileProblems(String token, FileNameDto fileNameDto) {
        String fileName = fileNameDto.getFileName();
        String fileId;
        List<AiGeneratedProblems> problems;

        fileId = problemFilesRepository.findFileIdByMemberIdAndFileName(token,fileName);
        problems = aiGeneratedProblemsRepository.findByFileId(fileId);

        return problems;
    }




}