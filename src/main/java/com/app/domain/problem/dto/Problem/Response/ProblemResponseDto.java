package com.app.domain.problem.dto.Problem.Response;

import com.app.domain.problem.entity.AiGeneratedProblem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProblemResponseDto {
    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;

    public static ProblemResponseDto ConvertToProblem(AiGeneratedProblem aiGenerateProblem){
        return ProblemResponseDto.builder()
                .problemName(aiGenerateProblem.getProblemName())
                .problemChoices(aiGenerateProblem.getProblemChoices())
                .problemAnswer(aiGenerateProblem.getProblemAnswer())
                .problemCommentary(aiGenerateProblem.getProblemCommentary())
                .build();
    }
}



