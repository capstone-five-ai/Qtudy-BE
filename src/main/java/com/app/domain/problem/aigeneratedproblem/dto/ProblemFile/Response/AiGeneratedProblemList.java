package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response;

import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Setter
@Getter
@Builder
@Schema(name = "AiGeneratedProblemList", description = "AI 생성 문제 목록 DTO")
public class AiGeneratedProblemList {
    @Schema(description = "AI 생성 문제 ID", example = "1")
    private Long aiGeneratedProblemId;

    @Schema(description = "문제 이름", example = "문제 이름 예시")
    private String problemName;

    @Schema(description = "문제 선택지", example = "[\"선택지1\", \"선택지2\"]")
    private List<String> problemChoices;

    @Schema(description = "문제 정답", example = "선택지1")
    private String problemAnswer;

    @Schema(description = "문제 해설", example = "문제 해설 예시")
    private String problemCommentary;

    public static AiGeneratedProblemList ConvertToProblem(AiGeneratedProblem aiGenerateProblem){
        return AiGeneratedProblemList.builder()
            .aiGeneratedProblemId(aiGenerateProblem.getProblemId())
            .problemName(aiGenerateProblem.getProblemName())
            .problemChoices(aiGenerateProblem.getProblemChoices())
            .problemAnswer(aiGenerateProblem.getProblemAnswer())
            .problemCommentary(aiGenerateProblem.getProblemCommentary())
            .build();
    }
}



