package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request;

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
@Schema(name = "AiGenerateProblemByFileDto", description = "파일을 통한 AI 문제 생성 요청 DTO")
public class AiGenerateProblemByFileDto {
    @Schema(description = "문제 유형", example = "MULTIPLE_CHOICE")
    private ProblemType type;

    @Schema(description = "문제 수", example = "TEN")
    private Amount amount;

    @Schema(description = "문제 난이도", example = "EASY")
    private ProblemDifficulty difficulty;

    @Schema(description = "파일 이름", example = "sample.pdf")
    private String fileName;
}
