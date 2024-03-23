package com.app.domain.summary.service;


import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.problem.entity.ProblemFile;
import com.app.domain.summary.dto.SummaryFile.AiRequest.AiGenerateSummaryFromAiDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.entity.SummaryFile;
import com.app.domain.summary.repository.SummaryFileRepository;
import com.app.global.config.ENUM.PdfType;
import com.app.global.config.S3.S3Service;
import com.app.domain.summary.repository.AiGeneratedSummaryRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import com.app.global.pdf.SummaryPdfMaker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private MemberService memberService;
    @Autowired
    private S3Service s3Service;

    public AiGeneratedSummary GetSummary(int fileId){


        Optional<AiGeneratedSummary> aiGeneratedSummaryOptional = aiGeneratedSummaryRepository.findBySummaryFile_FileId(fileId);
        if (aiGeneratedSummaryOptional.isPresent()) {
            return aiGeneratedSummaryOptional.get();
        } else {
            throw new BusinessException(ErrorCode.NOT_EXIST_PROBLEM); // 파일없을경우.
        }

    }

    public MemberSavedSummaryDto.pdfResponse createSummaryPdf(Integer summaryId) throws IOException {
        AiGeneratedSummary summary = findVerifiedSummaryBySummaryId(summaryId);

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

    public Boolean checkIsWriter(HttpServletRequest httpServletRequest, int fileId) {
        Member member = memberService.getLoginMember(httpServletRequest);
        Optional<SummaryFile> optionalProblemFile = Optional.ofNullable(summaryFileRepository.findByFileId(fileId));

        if(optionalProblemFile.isEmpty())
            throw new BusinessException(ErrorCode.NOT_EXIST_FILE); // 파일없을경우.

        if (optionalProblemFile.get().getMember().getMemberId() == member.getMemberId()) { // 인증 성공
            return true;
        }
        return false; // 인증 실패
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
}
