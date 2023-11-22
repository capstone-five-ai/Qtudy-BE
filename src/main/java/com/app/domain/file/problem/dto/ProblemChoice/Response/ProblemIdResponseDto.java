package com.app.domain.file.problem.dto.ProblemChoice.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemIdResponseDto {
    private int aiProblemChoiceId;
    private String aiProblemChoiceContent;
}
