package com.app.domain.aicreate.problem.dto;

import com.app.domain.aicreate.common.ENUM.Difficulty;
import com.app.domain.aicreate.common.ENUM.Amount;
import com.app.domain.aicreate.common.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProblemDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private Difficulty difficulty;

}
