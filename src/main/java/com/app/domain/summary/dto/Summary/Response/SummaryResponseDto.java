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
    private Response response;

    public static SummaryResponseDto ConvertToSummary(boolean isWriter, AiGeneratedSummary aiGeneratedSummary) {
        return SummaryResponseDto.builder()
                .isWriter(isWriter)
                .response(Response.builder()
                        .aiGeneratedSummaryId(aiGeneratedSummary.getAiGeneratedSummaryId())
                        .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                        .summaryContent(aiGeneratedSummary.getSummaryContent())
                        .build())
                .build();
    }

    public boolean getIsWriter() {
        return isWriter;
    }

    @Builder
    @Getter
    public static class Response {
        private int aiGeneratedSummaryId;
        private String summaryTitle;
        private String summaryContent;
    }

}