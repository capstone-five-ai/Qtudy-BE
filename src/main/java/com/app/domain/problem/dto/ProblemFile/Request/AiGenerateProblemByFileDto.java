package com.app.domain.problem.dto.ProblemFile.Request;

import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemByFileDto {
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;
    private String fileName;


}
