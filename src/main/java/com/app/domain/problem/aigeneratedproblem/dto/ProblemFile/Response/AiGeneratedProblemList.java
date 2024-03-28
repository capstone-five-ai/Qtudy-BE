package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response;

import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Setter
@Getter
@Builder
public class AiGeneratedProblemList {
    private int aiGeneratedProblemId;
    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;

    public static AiGeneratedProblemList ConvertToProblem(AiGeneratedProblem aiGenerateProblem){
        return AiGeneratedProblemList.builder()
                .aiGeneratedProblemId(aiGenerateProblem.getProblemId().intValue())
                .problemName(aiGenerateProblem.getProblemName())
                .problemChoices(aiGenerateProblem.getProblemChoices())
                .problemAnswer(aiGenerateProblem.getProblemAnswer())
                .problemCommentary(aiGenerateProblem.getProblemCommentary())
                .build();
    }
}



