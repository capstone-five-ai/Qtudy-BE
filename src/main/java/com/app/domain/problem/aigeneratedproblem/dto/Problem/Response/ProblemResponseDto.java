package com.app.domain.problem.aigeneratedproblem.dto.Problem.Response;

import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@Setter
@Builder
@Schema(name = "ProblemResponseDto", description = "문제 응답 DTO")
public class ProblemResponseDto {
    @JsonProperty("isWriter")
    @Schema(description = "작성자인지 여부", example = "true")
    private boolean isWriter;

    @Schema(description = "AI 생성 문제 목록", implementation = AiGeneratedProblemList.class)
    private List<AiGeneratedProblemList> problems;

    public static ProblemResponseDto ConvertToProblem(boolean isWriter, List<AiGeneratedProblemList> aiGeneratedProblemList){
        return ProblemResponseDto.builder()
            .isWriter(isWriter)
            .problems(aiGeneratedProblemList)
            .build();
    }

    public boolean getIsWriter() {
        return isWriter;
    }

    public List<AiGeneratedProblemList> getProblems() {
        return problems;
    }
}


