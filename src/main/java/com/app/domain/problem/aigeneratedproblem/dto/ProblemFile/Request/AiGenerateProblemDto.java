package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request;

import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest.RequestProblemToAiByTextDto;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "AiGenerateProblemDto", description = "AI 문제 생성 요청 DTO")
public class AiGenerateProblemDto {
    @Schema(description = "텍스트", example = "샘플 텍스트")
    private String text;

    @Schema(description = "문제 유형", example = "MULTIPLE_CHOICE")
    private ProblemType type;

    @Schema(description = "문제 수", example = "TEN")
    private Amount amount;

    @Schema(description = "문제 난이도", example = "EASY")
    private ProblemDifficulty difficulty;

    @Schema(description = "파일 이름", example = "sample.pdf")
    private String fileName;

    public RequestProblemToAiByTextDto toTextDto(String pdfText) {
        RequestProblemToAiByTextDto textDto = new RequestProblemToAiByTextDto();
        textDto.setText(pdfText);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        return textDto;
    }
}
