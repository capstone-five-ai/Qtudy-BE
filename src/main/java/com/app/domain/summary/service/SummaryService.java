package com.app.domain.summary.service;

import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.domain.summary.dto.SummaryDto;
import com.app.domain.summary.entity.Summary;
import com.app.domain.summary.repository.SummaryRepository;
import com.app.global.config.ENUM.PdfType;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import com.app.global.pdf.SummaryPdfMaker;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;

    @Transactional(readOnly = true)
    public Summary findVerifiedSummaryBySummaryId(Integer summaryId){
        return summaryRepository.findById(summaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.SUMMARY_NOT_EXISTS));
    }

    public SummaryDto.pdfResponse createSummaryPdf(Integer summaryId) throws IOException {
        Summary summary = findVerifiedSummaryBySummaryId(summaryId);

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
        return new SummaryDto.pdfResponse(byteArrayOutputStream.toByteArray(), summary.getSummaryTitle());
    }

    public void updateSummary(String summaryTitle, String summaryContent, Integer summaryId) {
        Summary preSummary = findVerifiedSummaryBySummaryId(summaryId);

        Optional.ofNullable(summaryTitle)
                .ifPresent(preSummary::updateSummaryTitle);
        Optional.ofNullable(summaryContent)
                .ifPresent(preSummary::updateSummaryContent);

        summaryRepository.save(preSummary);
    }
}
