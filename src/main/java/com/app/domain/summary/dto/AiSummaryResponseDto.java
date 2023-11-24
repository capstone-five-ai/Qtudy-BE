package com.app.domain.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiSummaryResponseDto {

    private String summaryTitle;
    private String summaryContent;
}
