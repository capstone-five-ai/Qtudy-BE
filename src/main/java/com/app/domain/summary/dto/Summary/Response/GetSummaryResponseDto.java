package com.app.domain.summary.dto.Summary.Response;

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
    private int aiGeneratedSummaryId;
    private String summaryTitle;
    private String summaryContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
