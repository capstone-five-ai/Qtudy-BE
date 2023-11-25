package com.app.domain.summary.dto.SummaryFile.AiRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateSummaryResponseDto {
    private String fileName;
    private String summaryTitle;
    private String summaryContent;
}
