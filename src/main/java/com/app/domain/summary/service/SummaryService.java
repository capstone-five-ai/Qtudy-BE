package com.app.domain.summary.service;

import com.app.domain.summary.dto.Summary.Request.GetSummaryRequestDto;
import com.app.domain.summary.dto.Summary.Request.UpdateSummaryRequestDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.entity.SummaryFile;
import com.app.domain.summary.repository.SummaryFileRepository;
import com.app.global.config.S3.S3Service;
import com.app.domain.summary.repository.AiGeneratedSummaryRepository;
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

    public AiGeneratedSummary GetSummary(String token, GetSummaryRequestDto getSummaryRequestDto){
        int aiGeneratedSummaryId = getSummaryRequestDto.getAiGeneratedSummaryId();

        Optional<AiGeneratedSummary> aiGeneratedSummaryOptional = aiGeneratedSummaryRepository.findById(aiGeneratedSummaryId);
        if (aiGeneratedSummaryOptional.isPresent()) {
            return aiGeneratedSummaryOptional.get();
        } else {
            return null; //추후 에러 처리 예정
        }

    }

    public AiGeneratedSummary UpdateSummary(String token, UpdateSummaryRequestDto updateSummaryRequestDto) {
        int aiGeneratedSummaryId = updateSummaryRequestDto.getAiGeneratedSummaryId();
        String summaryTitle = updateSummaryRequestDto.getSummaryTitle();
        String summaryContent = updateSummaryRequestDto.getSummaryContent();

        Optional<AiGeneratedSummary> aiGeneratedSummaryOptional = aiGeneratedSummaryRepository.findById(aiGeneratedSummaryId);
        if (aiGeneratedSummaryOptional.isPresent()) {
            AiGeneratedSummary aiGeneratedSummary = aiGeneratedSummaryOptional.get();
            aiGeneratedSummary.setSummaryTitle(summaryTitle);
            aiGeneratedSummary.setSummaryContent(summaryContent);

            aiGeneratedSummaryRepository.save(aiGeneratedSummary);

            return aiGeneratedSummary;
        } else {
            //추후 에러 처리 예정
        }
        return null;
    }


}
