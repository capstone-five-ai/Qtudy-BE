package com.app.domain.summary.dto.Summary.Response;

import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.global.config.ENUM.ProblemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetSummaryResponseDto {
    private String summaryTitle;
    private String summaryContent;

    public static GetSummaryResponseDto ConvertToSummary(AiGeneratedSummary aiGeneratedSummary){
        return GetSummaryResponseDto.builder()
                .summaryTitle(aiGeneratedSummary.getSummaryTitle())
                .summaryContent(aiGeneratedSummary.getSummaryContent())
                .build();
    }
}
