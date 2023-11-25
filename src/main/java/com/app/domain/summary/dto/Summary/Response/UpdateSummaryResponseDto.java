package com.app.domain.summary.dto.Summary.Response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateSummaryResponseDto {
    private int aiGeneratedSummaryId;
    private String summaryTitle;
    private String summaryContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
