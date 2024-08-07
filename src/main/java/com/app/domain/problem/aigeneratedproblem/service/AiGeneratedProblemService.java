package com.app.domain.problem.aigeneratedproblem.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.problem.membersavedproblem.entity.MemberSavedProblem;
import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.domain.problem.aigeneratedproblem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.aigeneratedproblem.repository.ProblemFileRepository;
import com.app.global.config.S3.S3Service;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class AiGeneratedProblemService { //Service 추후 분할 예정

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProblemFileRepository problemFileRepository;
    @Autowired
    private AiGeneratedProblemRepository aiGeneratedProblemRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private S3Service s3Service;



    public List<AiGeneratedProblem> GetFileProblems(Long fileId) {
        ProblemFile problemFile;
        List<AiGeneratedProblem> problems;

        problemFile = problemFileRepository.getByFileId(fileId);
        problems = aiGeneratedProblemRepository.findByProblemFile_FileId(problemFile.getFileId());

        return problems;
    }

    public AiGeneratedProblem GetProblem(String token,Long aiGeneratedProblemId) { //삭제 예정

        Optional<AiGeneratedProblem> optionalAiGeneratedProblem;

        optionalAiGeneratedProblem = aiGeneratedProblemRepository.findById(aiGeneratedProblemId);

        if(optionalAiGeneratedProblem.isPresent()) {
            AiGeneratedProblem aiGeneratedProblem = optionalAiGeneratedProblem.get();
            return aiGeneratedProblem;
        }

        return null; //추후 에러처리 예정
    }

    public Boolean checkIsWriter(HttpServletRequest httpServletRequest, Long fileId) {
        Member member = memberService.getLoginMember(httpServletRequest);
        ProblemFile problemFile = problemFileRepository.getByFileId(fileId);

        if (problemFile.getMember().getMemberId().equals(member.getMemberId())) { // 인증 성공
            return true;
        }
        return false; // 인증 실패
    }

    public AiGeneratedProblem updateProblem(MemberSavedProblem problem, Long problemId){
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

    public AiGeneratedProblem findVerifiedProblemByProblemId(Long problemId){
        return aiGeneratedProblemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROBLEM_NOT_EXISTS));
    }

}
