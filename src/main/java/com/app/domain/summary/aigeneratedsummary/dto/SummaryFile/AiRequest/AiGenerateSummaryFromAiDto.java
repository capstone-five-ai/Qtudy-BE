package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "AI가 생성한 요약 내용을 담는 DTO")
public class AiGenerateSummaryFromAiDto {
    @Schema(description = "AI가 생성한 요약 내용", example = "This is a summary generated by AI.")
    private String summaryContent;
}
