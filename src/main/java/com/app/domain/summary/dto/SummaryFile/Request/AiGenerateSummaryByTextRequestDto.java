package com.app.domain.summary.dto.SummaryFile.Request;

import com.app.domain.summary.dto.SummaryFile.AiRequest.TypeConvertSummaryDto;
import com.app.global.config.ENUM.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateSummaryByTextRequestDto {
    private String text;
    private Amount amount;
    private String fileName;

    public TypeConvertSummaryDto toTextDto2(){
        TypeConvertSummaryDto textDto = new TypeConvertSummaryDto();
        textDto.setAmount(this.amount);
        textDto.setFileName(fileName);
        return textDto;
    }

}
