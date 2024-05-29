package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response;

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
@Schema(name = "AiGeneratedProblemResponseDto", description = "AI 생성 문제 응답 DTO")
public class AiGeneratedProblemResponseDto {
    @Schema(description = "파일 ID", example = "1")
    private int fileId;

    @Schema(description = "AI 생성 문제 목록", implementation = AiGeneratedProblemList.class)
    private List<AiGeneratedProblemList> problems;

    public static AiGeneratedProblemResponseDto ConvertToProblem(List<AiGeneratedProblemList> aiGeneratedProblemList, int fileId){
        return AiGeneratedProblemResponseDto.builder()
            .fileId(fileId)
            .problems(aiGeneratedProblemList)
            .build();
    }
}



