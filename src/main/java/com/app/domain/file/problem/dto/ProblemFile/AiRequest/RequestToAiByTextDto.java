package com.app.domain.file.problem.dto.ProblemFile.AiRequest;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.problem.entity.ProblemFiles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestToAiByTextDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;

    public ProblemFiles toEntity() { // DTO -> ENTITY 변환 메서드
        return ProblemFiles.builder()
                //.memberId(1)     (pk값이라 일단 제거)
                .dtype(DType.PROBLEM)
                .problemDifficulty(difficulty)
                .problemAmount(amount)
                .problemType(type)
                .build();
    }

}
