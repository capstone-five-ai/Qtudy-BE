package com.app.domain.file.summary.dto;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.summary.entity.SummaryFiles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SummaryDto {
    private String text;
    private Amount amount;

    public SummaryFiles toEntity() {
        return SummaryFiles.builder()
                .memberId(1)
                .dtype(DType.SUMMARY)
                .summaryAmount(amount)
                .build();
    }
}
