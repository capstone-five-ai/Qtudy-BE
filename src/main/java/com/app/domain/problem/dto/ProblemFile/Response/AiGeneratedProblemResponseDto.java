package com.app.domain.problem.dto.ProblemFile.Response;

import com.app.domain.problem.entity.AiGeneratedProblem;
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



