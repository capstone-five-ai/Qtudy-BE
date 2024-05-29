package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request;

import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest.RequestSummaryToAiByTextDto;
import com.app.global.config.ENUM.Amount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "AI 요약 파일 생성 요청 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateSummaryDto {

    @Schema(description = "요약할 텍스트", example = "요약할 텍스트 예시")
    private String text;

    @Schema(description = "요약 양", example = "FULL")
    private Amount amount;

    @Schema(description = "파일 이름", example = "요약파일명")
    private String fileName;

    public RequestSummaryToAiByTextDto toTextDto(String text) {
        RequestSummaryToAiByTextDto textDto = new RequestSummaryToAiByTextDto();
        textDto.setText(text);
        textDto.setAmount(this.amount);
        textDto.setFileName(this.fileName);
        return textDto;
    }
}
