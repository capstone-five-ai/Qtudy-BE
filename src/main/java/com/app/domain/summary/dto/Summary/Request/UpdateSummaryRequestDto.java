package com.app.domain.summary.dto.Summary.Request;

import com.app.domain.summary.dto.SummaryFile.AiRequest.RequestSummaryToAiByTextDto;
import com.app.domain.summary.dto.SummaryFile.AiRequest.TypeConvertSummaryDto;
import com.app.global.config.ENUM.Amount;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateSummaryRequestDto {
    private String summaryTitle;
    private String summaryContent;

}
