package com.app.domain.aicreate.summary.dto;

import com.app.domain.aicreate.common.ENUM.Difficulty;
import com.app.domain.aicreate.common.ENUM.Amount;
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
    private Difficulty difficulty;
}
