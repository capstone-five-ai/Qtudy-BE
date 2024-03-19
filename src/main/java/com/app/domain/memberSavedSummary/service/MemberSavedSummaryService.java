package com.app.domain.memberSavedSummary.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.memberSavedSummary.repository.MemberSavedSummaryRepository;
import com.app.domain.summary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.global.config.ENUM.PdfType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import com.app.global.pdf.SummaryPdfMaker;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jboss.jandex.Main;
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

        File tempFile = SummaryPdfMaker.CreatePdfFile(summary.getSummaryTitle(), new AiGenerateSummaryFromAiDto(summary.getSummaryContent()), PdfType.SUMMARY);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // try-with-resources 구문 사용
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
