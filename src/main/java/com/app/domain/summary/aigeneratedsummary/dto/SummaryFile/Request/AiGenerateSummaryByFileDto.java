package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request;

import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.RequestSummaryToAiByTextDto;
import com.app.global.config.ENUM.Amount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "파일 기반 요약 생성을 요청하는 DTO")
public class AiGenerateSummaryByFileDto {
    @Schema(description = "요약의 양", example = "SHORT")
    private Amount amount;

    @Schema(description = "파일 이름", example = "summary.txt")
    private String fileName;

    public RequestSummaryToAiByTextDto toTextDto(String text) {
        RequestSummaryToAiByTextDto textDto = new RequestSummaryToAiByTextDto();
        textDto.setText(text); // Assuming you have a method to convert MultipartFile to text
        textDto.setAmount(this.amount);
        textDto.setFileName(this.fileName);
        return textDto;
    }
}
