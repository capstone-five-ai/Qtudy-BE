package com.app.domain.summary.membersavedsummary.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import com.app.domain.summary.membersavedsummary.repository.MemberSavedSummaryRepository;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.global.config.ENUM.PdfType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import com.app.global.pdf.SummaryPdfMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public MemberSavedSummaryDto.pdfResponse createSummaryPdf(Long memberSavedSummaryId) throws IOException {
        MemberSavedSummary summary = findVerifiedSummaryBySummaryId(memberSavedSummaryId);

        // SummaryPdfMaker를 사용하여 PDF 파일 생성
        File tempFile = SummaryPdfMaker.CreatePdfFile(summary.getSummaryTitle(), new AiGenerateSummaryFromAiDto(summary.getSummaryContent()), PdfType.SUMMARY);

        // PDF 파일을 바이트 배열로 변환
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace(); // 에러 처리
        }

        // 바이트 배열을 사용자에게 반환
        return new MemberSavedSummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), summary.getSummaryTitle());
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
    public MemberSavedSummary findVerifiedSummaryBySummaryId(Long summaryId){
        return memberSavedSummaryRepository.findById(summaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUMMARY_NOT_EXISTS));
    }

    public Boolean checkIsWriter(HttpServletRequest httpServletRequest, MemberSavedSummary memberSavedSummary) {
        Member member = memberService.getLoginMember(httpServletRequest);
        if (memberSavedSummary.getMember().getMemberId() == member.getMemberId()) {
            return true;
        }
        return false;
    }
}
