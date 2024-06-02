package com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.AiRequest;

import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "텍스트 기반 요약 생성을 요청하는 DTO")
public class RequestSummaryToAiByTextDto {
    @Schema(description = "요약할 텍스트", example = "This is the text to be summarized.")
    private String text;

    @Schema(description = "문제 유형", example = "MULTIPLE_CHOICE")
    private ProblemType type;

    @Schema(description = "요약의 양", example = "SHORT")
    private Amount amount;

    @Schema(description = "문제의 난이도", example = "EASY")
    private ProblemDifficulty difficulty;

    @Schema(description = "파일 이름", example = "summary.txt")
    private String fileName;
}
