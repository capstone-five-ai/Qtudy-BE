package com.app.domain.summary.service;


import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.repository.SummaryFileRepository;
import com.app.global.config.S3.S3Service;
import com.app.domain.summary.repository.AiGeneratedSummaryRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class SummaryService {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryFileRepository summaryFileRepository;
    @Autowired
    private AiGeneratedSummaryRepository aiGeneratedSummaryRepository;
    @Autowired
    private S3Service s3Service;

    public AiGeneratedSummary GetSummary(String token,int aiGeneratedSummaryId){

        Optional<AiGeneratedSummary> aiGeneratedSummaryOptional = aiGeneratedSummaryRepository.findById(aiGeneratedSummaryId);
        if (aiGeneratedSummaryOptional.isPresent()) {
            return aiGeneratedSummaryOptional.get();
        } else {
            return null; //추후 에러 처리 예정
        }

    }

    public MemberSavedSummaryDto.pdfResponse createSummaryPdf(Integer summaryId) throws IOException {
        AiGeneratedSummary summary = findVerifiedSummaryBySummaryId(summaryId);

        try (PDDocument document = new PDDocument()) {
            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/malgun.ttf"));
            PDPage page = new PDPage();
            document.addPage(page);

            // 제목 추가
            addTitle(document, page, summary.getSummaryTitle(), font);

            // 내용 추가
            addContent(document, page, summary.getSummaryContent(), font);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return new MemberSavedSummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), summary.getSummaryTitle());
        }
    }
    public AiGeneratedSummary updateSummary(MemberSavedSummary summary, Integer summaryId){
        AiGeneratedSummary preSummary = findVerifiedSummaryBySummaryId(summaryId);
        Optional.ofNullable(summary.getSummaryTitle())
                .ifPresent(summaryTitle -> preSummary.updateSummaryTitle(summaryTitle));
        Optional.ofNullable(summary.getSummaryContent())
                .ifPresent(summaryContent -> preSummary.updateSummaryContent(summaryContent));
        return aiGeneratedSummaryRepository.save(preSummary);
    }

    public AiGeneratedSummary findVerifiedSummaryBySummaryId(Integer aiGeneratedSummaryId) {
        return aiGeneratedSummaryRepository.findById(aiGeneratedSummaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUMMARY_NOT_EXISTS));
    }


    private void addTitle(PDDocument document, PDPage page, String title, PDType0Font font) throws IOException {
        float titleFontSize = 18; // 제목의 글자 크기
        float margin = 50; // 여백
        float startY = page.getMediaBox().getHeight() - margin;

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(font, titleFontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, startY);
            contentStream.showText(title);
            contentStream.endText();
        }
    }

    private void addContent(PDDocument document, PDPage page, String content, PDType0Font font) throws IOException {
        float fontSize = 12; // 본문의 글자 크기
        float leading = 1.5f * fontSize; // 줄 간격
        float margin = 50; // 여백
        float width = page.getMediaBox().getWidth() - 2 * margin;
        float startY = page.getMediaBox().getHeight() - margin - leading - leading; // 본문 시작 위치

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        contentStream.setFont(font, fontSize);

        String[] lines = content.split("\n");
        for (String line : lines) {
            startY = addLine(document, contentStream, line, font, fontSize, width, margin, startY, leading);
            if (startY < margin) { // 페이지 하단에 도달하면 새 페이지 시작
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(font, fontSize);
                startY = page.getMediaBox().getHeight() - margin - leading - leading;
            }
        }

        contentStream.close();
    }

    private float addLine(PDDocument document, PDPageContentStream contentStream, String line, PDType0Font font, float fontSize, float width, float margin, float startY, float leading) throws IOException {
        float currentX = margin;
        contentStream.beginText();
        contentStream.newLineAtOffset(currentX, startY);

        String[] words = line.split(" ");
        for (String word : words) {
            float wordWidth = font.getStringWidth(word) / 1000 * fontSize;
            if (currentX + wordWidth >= width) {
                contentStream.endText();
                startY -= leading;
                if (startY < margin) { // 페이지 하단에 도달하면 새 페이지 시작
                    contentStream.close();
                    PDPage page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontSize);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, page.getMediaBox().getHeight() - margin - leading);
                    startY = page.getMediaBox().getHeight() - margin - leading;
                } else {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, startY);
                }
                currentX = margin;
            }
            contentStream.showText(word + " ");
            currentX += wordWidth + font.getStringWidth(" ") / 1000 * fontSize;
        }
        contentStream.endText();
        return startY - leading;
    }
}
