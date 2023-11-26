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
    private int aiGeneratedProblemId;
    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;

    public static AiGeneratedProblemResponseDto ConvertToProblem(AiGeneratedProblem aiGenerateProblem){
        return AiGeneratedProblemResponseDto.builder()
                .aiGeneratedProblemId(aiGenerateProblem.getAiGeneratedProblemId())
                .problemName(aiGenerateProblem.getProblemName())
                .problemChoices(aiGenerateProblem.getProblemChoices())
                .problemAnswer(aiGenerateProblem.getProblemAnswer())
                .problemCommentary(aiGenerateProblem.getProblemCommentary())
                .build();
    }
}



