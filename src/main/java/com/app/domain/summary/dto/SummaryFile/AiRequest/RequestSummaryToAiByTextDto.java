package com.app.domain.summary.dto.SummaryFile.AiRequest;

import com.app.domain.problem.entity.ProblemFile;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.DType;
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
public class RequestSummaryToAiByTextDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;
    private String fileName;

    public ProblemFile toEntity() { // DTO -> ENTITY 변환 메서드
        return ProblemFile.builder()
                //.memberId(1)     (pk값이라 일단 제거)
                .dtype(DType.PROBLEM)
                .problemDifficulty(difficulty)
                .problemAmount(amount)
                .problemType(type)
                .build();
    }

}
