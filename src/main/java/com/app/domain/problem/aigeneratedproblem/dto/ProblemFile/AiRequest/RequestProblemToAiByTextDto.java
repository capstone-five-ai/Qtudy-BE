package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest;

import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestProblemToAiByTextDto {
    private String text;
    private Amount amount;
    private ProblemDifficulty difficulty;
}
