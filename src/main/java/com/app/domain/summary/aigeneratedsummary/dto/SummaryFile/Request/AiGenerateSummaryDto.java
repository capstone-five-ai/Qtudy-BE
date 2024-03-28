package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request;

import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.RequestSummaryToAiByTextDto;
import com.app.global.config.ENUM.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateSummaryDto {
    private String text;
    private Amount amount;
    private String fileName;

    public RequestSummaryToAiByTextDto toTextDto(String text) {
        RequestSummaryToAiByTextDto textDto = new RequestSummaryToAiByTextDto();
        textDto.setText(text); // Assuming you have a method to convert MultipartFile to text
        textDto.setAmount(this.amount);
        textDto.setFileName(this.fileName);
        return textDto;
    }

}
