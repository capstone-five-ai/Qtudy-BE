package com.app.domain.summary.dto.Summary.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetSummaryRequestDto {
    private int aiGeneratedSummaryId;
}
