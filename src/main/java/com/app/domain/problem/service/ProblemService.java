package com.app.domain.problem.service;

import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.entity.ProblemFile;
import com.app.domain.problem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.repository.ProblemFileRepository;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
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

    public AiGeneratedProblem updateProblem(MemberSavedProblem problem, Integer problemId){
        AiGeneratedProblem preProblem = findVerifiedProblemByProblemId(problemId);
        Optional.ofNullable(problem.getProblemName())
                .ifPresent(problemName -> preProblem.updateProblemName(problemName));
        Optional.ofNullable(problem.getProblemAnswer())
                .ifPresent(problemAnswer -> preProblem.updateProblemAnswer(problemAnswer));
        Optional.ofNullable(problem.getProblemCommentary())
                .ifPresent(problemCommentary -> preProblem.updateProblemCommentary(problemCommentary));
        Optional.ofNullable(problem.getProblemChoices())
                .ifPresent(problemChoices ->{
                    preProblem.getProblemChoices().clear();
                    preProblem.getProblemChoices().addAll(problemChoices);
                });
        return aiGeneratedProblemRepository.save(preProblem);
    }

    public AiGeneratedProblem findVerifiedProblemByProblemId(Integer problemId){
        return aiGeneratedProblemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROBLEM_NOT_EXISTS));
    }

}