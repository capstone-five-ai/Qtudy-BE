package com.app.domain.problem.dto.Problem.Response;


import com.app.global.config.ENUM.ProblemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetProblemResponseDto {
    private int aiGeneratedProblemId;
    private String problemName;
    private List<String> problemChoices; // Assuming AiProblemChoice has appropriate DTO or can be mapped directly
    private String problemAnswer;
    private String problemCommentary;
}
