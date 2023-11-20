package com.app.domain.file.problem.service;

import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.Problem.Request.FileNameRequestDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.ProblemFiles;
import com.app.domain.file.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.file.problem.repository.AiProblemChoiceRepository;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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



    public List<AiGeneratedProblems> GetFileProblems(String token, FileNameRequestDto fileNameRequestDto) {
        String fileName = fileNameRequestDto.getFileName();
        ProblemFiles problemFiles;
        List<AiGeneratedProblems> problems;

        problemFiles = problemFilesRepository.findByMemberIdAndFileName(token,fileName);
        problems = aiGeneratedProblemsRepository.findByProblemFiles_FileId(problemFiles.getFileId());

        return problems;
    }




}