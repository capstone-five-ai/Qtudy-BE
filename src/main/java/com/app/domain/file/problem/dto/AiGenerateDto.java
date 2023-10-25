package com.app.domain.file.problem.dto;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.Difficulty;
import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.file.problem.entity.ProblemFiles;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private Difficulty difficulty;

    public ProblemFiles toEntity() { // DTO -> ENTITY 변환 메서드
        return ProblemFiles.builder()
                .memberId(1)
                .dtype(DType.PROBLEM)
                .difficulty(difficulty)
                .problemAmount(amount)
                .problemType(type)
                .build();


    }

}
