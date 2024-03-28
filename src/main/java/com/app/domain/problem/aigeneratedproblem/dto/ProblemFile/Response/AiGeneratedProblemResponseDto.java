package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Setter
@Getter
@Builder
public class AiGeneratedProblemResponseDto {
    private int fileId;
    private List<AiGeneratedProblemList> problems;

    public static AiGeneratedProblemResponseDto ConvertToProblem(List<AiGeneratedProblemList> aiGeneratedProblemList, int fileId){
        return AiGeneratedProblemResponseDto.builder()
                .fileId(fileId)
                .problems(aiGeneratedProblemList)
                .build();
    }
}



