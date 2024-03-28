package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Response;

import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AiGenerateSummaryResponseDto {
    private int fileId;

    private int aiGeneratedSummaryId;
    private String summaryTitle;
    private String summaryContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;



    public static AiGenerateSummaryResponseDto ConvertToSummaryFileResponse(
            AiGeneratedSummary aiGeneratedSummary,int fileId){
        AiGenerateSummaryResponseDto aiGenerateSummaryResponseDto = AiGenerateSummaryResponseDto.builder()
                .fileId(fileId)
                .aiGeneratedSummaryId(aiGeneratedSummary.getSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
                .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
                .build();

        return aiGenerateSummaryResponseDto;
    }



}
