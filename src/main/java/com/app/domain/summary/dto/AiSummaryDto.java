package com.app.domain.summary.dto;

import com.app.global.config.ENUM.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiSummaryDto {
    private String text;
    private Amount amount;

}
