package com.app.domain.summary.dto.Summary.Response;

import com.app.domain.summary.entity.AiGeneratedSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SummaryResponseDto {
    @JsonProperty("isWriter")
    private boolean isWriter;
    private int aiGeneratedSummaryId;
    private String summaryTitle;
    private String summaryContent;

    public static SummaryResponseDto ConvertToSummary(boolean isWriter, AiGeneratedSummary aiGeneratedSummary){
        return SummaryResponseDto.builder()
                .isWriter(isWriter)
                .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .build();
    }

    public boolean getIsWriter() {
        return isWriter;
    }

    public int getAiGeneratedSummaryId() {
        return aiGeneratedSummaryId;
    }

    public String getSummaryTitle() {
        return summaryTitle;
    }

    public String getSummaryContent() {
        return summaryContent;
    }
}
