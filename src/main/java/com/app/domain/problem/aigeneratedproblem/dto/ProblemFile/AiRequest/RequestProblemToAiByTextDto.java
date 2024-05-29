package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest;

import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "RequestProblemToAiByTextDto", description = "텍스트를 통한 AI 문제 요청 DTO")
public class RequestProblemToAiByTextDto {
    @Schema(description = "텍스트", example = "샘플 텍스트")
    private String text;

    @Schema(description = "문제 수", example = "TEN")
    private Amount amount;

    @Schema(description = "문제 난이도", example = "EASY")
    private ProblemDifficulty difficulty;
}
