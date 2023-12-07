package com.app.domain.summary.service;


import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.repository.SummaryFileRepository;
import com.app.global.config.S3.S3Service;
import com.app.domain.summary.repository.AiGeneratedSummaryRepository;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
