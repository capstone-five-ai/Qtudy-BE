package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Response;

import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "AI 생성 요약 파일 응답 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AiGenerateSummaryResponseDto {

    @Schema(description = "파일 ID", example = "1")
    private Long fileId;

    @Schema(description = "AI 생성 요약 ID", example = "1")
    private Long aiGeneratedSummaryId;

    @Schema(description = "요약 제목", example = "요약 제목 예시")
    private String summaryTitle;

    @Schema(description = "요약 내용", example = "요약 내용 예시")
    private String summaryContent;

    @Schema(description = "생성 시간", example = "2023-05-30T10:15:30")
    private LocalDateTime createTime;

    @Schema(description = "업데이트 시간", example = "2023-05-30T10:15:30")
    private LocalDateTime updateTime;

    public static AiGenerateSummaryResponseDto ConvertToSummaryFileResponse(AiGeneratedSummary aiGeneratedSummary, Long fileId) {
        return AiGenerateSummaryResponseDto.builder()
            .fileId(fileId)
            .aiGeneratedSummaryId(aiGeneratedSummary.getSummaryId())
            .summaryTitle(aiGeneratedSummary.getSummaryTitle())
            .summaryContent(aiGeneratedSummary.getSummaryContent())
            .createTime(aiGeneratedSummary.getSummaryFile().getCreateTime())
            .updateTime(aiGeneratedSummary.getSummaryFile().getUpdateTime())
            .build();
    }
}
