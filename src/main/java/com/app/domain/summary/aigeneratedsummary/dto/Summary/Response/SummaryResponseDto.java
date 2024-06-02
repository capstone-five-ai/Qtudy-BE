package com.app.domain.summary.aigeneratedsummary.dto.Summary.Response;

import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "요약 응답 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SummaryResponseDto {

    @Schema(description = "작성자 여부", example = "true")
    @JsonProperty("isWriter")
    private boolean isWriter;

    @Schema(description = "요약 응답 내용")
    private Response response;

    public static SummaryResponseDto ConvertToSummary(boolean isWriter, AiGeneratedSummary aiGeneratedSummary) {
        return SummaryResponseDto.builder()
            .isWriter(isWriter)
            .response(Response.builder()
                .aiGeneratedSummaryId(aiGeneratedSummary.getSummaryId())
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
    @Schema(description = "요약 응답 내부 클래스")
    public static class Response {
        @Schema(description = "AI 생성 요약 ID", example = "1")
        private Long aiGeneratedSummaryId;

        @Schema(description = "요약 제목", example = "샘플 요약")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "요약 내용")
        private String summaryContent;
    }
}
