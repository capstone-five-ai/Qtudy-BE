package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "AiGenerateProblemFromAiDto", description = "AI로부터 생성된 문제 DTO")
public class AiGenerateProblemFromAiDto {
    @Schema(description = "문제 이름", example = "샘플 문제")
    private String problemName;

    @Schema(description = "문제 선택지", example = "[\"선택지1\", \"선택지2\"]")
    private List<String> problemChoices;

    @Schema(description = "문제 정답", example = "선택지1")
    private String problemAnswer;

    @Schema(description = "문제 해설", example = "문제 해설 예시")
    private String problemCommentary;

    public static AiGenerateProblemFromAiDto create(String problemName, List<String> problemChoices, String problemAnswer, String problemCommentary) {
        return new AiGenerateProblemFromAiDto(problemName, problemChoices, problemAnswer, problemCommentary);
    }
}
