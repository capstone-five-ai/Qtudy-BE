package com.app.domain.problem.service;

import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.entity.ProblemFile;
import com.app.domain.problem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.repository.ProblemFileRepository;
import com.app.global.config.S3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFileRepository problemFileRepository;
    @Autowired
    private AiGeneratedProblemRepository aiGeneratedProblemRepository;
    @Autowired
    private S3Service s3Service;



    public List<AiGeneratedProblem> GetFileProblems(String token, int fileId) {
        ProblemFile problemFile;
        List<AiGeneratedProblem> problems;

        problemFile = problemFileRepository.findByMemberIdAndFileId(token,fileId);
        problems = aiGeneratedProblemRepository.findByProblemFile_FileId(problemFile.getFileId());

        return problems;
    }

    public AiGeneratedProblem GetProblem(String token,int aiGeneratedProblemId) {

        Optional<AiGeneratedProblem> optionalAiGeneratedProblem;

        optionalAiGeneratedProblem = aiGeneratedProblemRepository.findById(aiGeneratedProblemId);

        if(optionalAiGeneratedProblem.isPresent()) {
            AiGeneratedProblem aiGeneratedProblem = optionalAiGeneratedProblem.get();
            return aiGeneratedProblem;
        }

        return null; //추후 에러처리 예정
    }


}