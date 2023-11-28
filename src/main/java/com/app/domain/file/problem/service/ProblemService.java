package com.app.domain.file.problem.service;

import com.app.domain.file.common.service.S3Service;
import com.app.domain.file.problem.dto.Problem.Request.CommentaryRequestDto;
import com.app.domain.file.problem.dto.Problem.Request.FileNameRequestDto;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.ProblemFiles;
import com.app.domain.file.problem.repository.AiGeneratedProblemsRepository;
import com.app.domain.file.problem.repository.AiProblemChoiceRepository;
import com.app.domain.file.problem.repository.ProblemFilesRepository;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
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

    public void UpdateProblemCommentary(String token, CommentaryRequestDto commentaryRequestDto){
        int aiGeneratedProblemId = commentaryRequestDto.getAiGeneratedProblemId();
        String commentary = commentaryRequestDto.getCommentary();

        Optional<AiGeneratedProblems> optionalAiGeneratedProblems = aiGeneratedProblemsRepository.findById(aiGeneratedProblemId);

        // 엔티티가 존재하는 경우에만 업데이트 수행
        if (optionalAiGeneratedProblems.isPresent()) {
            AiGeneratedProblems aiGeneratedProblems = optionalAiGeneratedProblems.get();
            aiGeneratedProblems.setProblemCommentary(commentary);
            aiGeneratedProblemsRepository.save(aiGeneratedProblems);
        } else {
            // 해당 ID의 엔티티가 존재하지 않는 경우에 대한 처리 (생략 가능)
            // 예를 들어, 로깅 등을 수행할 수 있습니다.
            throw new BusinessException(ErrorCode.NOT_SENT_HTTP);
        }

    }

    public AiGeneratedProblems updateProblem(MemberSavedProblem problem, Integer problemId){
        AiGeneratedProblems preProblem = findVerifiedProblemByProblemId(problemId);
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
        return aiGeneratedProblemsRepository.save(preProblem);
    }

    public AiGeneratedProblems findVerifiedProblemByProblemId(Integer problemId){
        return aiGeneratedProblemsRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROBLEM_NOT_EXISTS));
    }


}