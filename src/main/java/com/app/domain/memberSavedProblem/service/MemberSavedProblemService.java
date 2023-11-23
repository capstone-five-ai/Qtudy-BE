package com.app.domain.memberSavedProblem.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedProblem.repository.MemberSavedProblemRepository;
import com.app.global.error.exception.EntityNotFoundException;
import com.app.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSavedProblemService {

    private final MemberService memberService;

    private final MemberSavedProblemRepository memberSavedProblemRepository;
    public MemberSavedProblem createProblem(MemberSavedProblem memberSavedProblem, HttpServletRequest httpServletRequest){
        Member member = memberService.getLoginMember(httpServletRequest);
        memberSavedProblem.updateMember(member);
        return memberSavedProblemRepository.save(memberSavedProblem);
    }

    public MemberSavedProblem updateProblem(MemberSavedProblem problem, Long problemId){
        MemberSavedProblem preProblem = findVerifiedProblemByProblemId(problemId);
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
        return memberSavedProblemRepository.save(preProblem);
    }

    public void deleteProblem(Long problemId){
        MemberSavedProblem problem = findVerifiedProblemByProblemId(problemId);

        memberSavedProblemRepository.deleteById(problemId);
    }
    private MemberSavedProblem findVerifiedProblemByProblemId(Long problemId){
        return memberSavedProblemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROBLEM_NOT_EXISTS));
    }
}
