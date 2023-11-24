package com.app.domain.memberSavedSummary.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.memberSavedSummary.repository.MemberSavedSummaryRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSavedSummaryService {

    private final MemberService memberService;

    private final MemberSavedSummaryRepository memberSavedSummaryRepository;

    public MemberSavedSummary createSummary(MemberSavedSummary memberSavedSummary, HttpServletRequest httpServletRequest) {
        Member member = memberService.getLoginMember(httpServletRequest);
        memberSavedSummary.updateMember(member);

        return memberSavedSummaryRepository.save(memberSavedSummary);
    }

    public MemberSavedSummary updateSummary(MemberSavedSummary summary, Long summaryId) {
        MemberSavedSummary preSummary = findVerifiedSummaryBySummaryId(summaryId);

        Optional.ofNullable(summary.getSummaryTitle())
                .ifPresent(summaryTitle -> preSummary.updateSummaryTitle(summaryTitle));
        Optional.ofNullable(summary.getSummaryContent())
                .ifPresent(summaryContent -> preSummary.updateSummaryContent(summaryContent));

        return memberSavedSummaryRepository.save(preSummary);
    }

    public void deleteSummary(Long summaryId){
        MemberSavedSummary sumamry = findVerifiedSummaryBySummaryId(summaryId);

        memberSavedSummaryRepository.deleteById(summaryId);
    }
    private MemberSavedSummary findVerifiedSummaryBySummaryId(Long summaryId){
        return memberSavedSummaryRepository.findById(summaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUMMARY_NOT_EXISTS));
    }
}
