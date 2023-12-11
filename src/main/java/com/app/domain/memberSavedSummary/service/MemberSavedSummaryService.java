package com.app.domain.memberSavedSummary.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.memberSavedSummary.repository.MemberSavedSummaryRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
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

        try (PDDocument document = new PDDocument()) {
            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/malgun.ttf"));
            float fontSize = 12;
            float titleFontSize = 18; // 제목의 글자 크기를 더 크게 설정
            float leading = 1.5f * fontSize;
            float margin = 50;
            PDPage page = new PDPage();
            document.addPage(page);
            float width = page.getMediaBox().getWidth() - 2 * margin;
            float startX = margin;
            float startY = page.getMediaBox().getHeight() - margin;

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, titleFontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            // 제목 추가
            String title = summary.getSummaryTitle();
            float titleWidth = font.getStringWidth(title) / 1000 * titleFontSize;
            // 가운데 정렬을 위해 계산
            float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
            contentStream.newLineAtOffset(titleX - startX, 0); // 제목의 시작 위치 조정
            contentStream.showText(title);
            contentStream.endText();

            // 본문 내용 시작 위치를 제목 아래로 조정
            startY -= titleFontSize + leading;

            // 본문 폰트 크기 설정
            contentStream.setFont(font, fontSize);

            String[] lines = summary.getSummaryContent().split("\n");
            for (String line : lines) {
                String[] words = line.split(" ");
                float currentX = startX;
                float currentY = startY;
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, currentY);
                for (String word : words) {
                    float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
                    if (currentX + wordWidth >= width) {
                        contentStream.endText();
                        currentY -= leading;
                        currentX = startX;
                        if (currentY <= margin) {
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.setFont(font, fontSize);
                            currentY = page.getMediaBox().getHeight() - margin;
                        }
                        contentStream.beginText();
                        contentStream.newLineAtOffset(startX, currentY);
                    }
                    contentStream.showText(word + " ");
                    currentX += wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
                }
                contentStream.endText();
                startY = currentY - leading;
            }

            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return new MemberSavedSummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), summary.getSummaryTitle());
        }
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
