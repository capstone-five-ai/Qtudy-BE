package com.app.domain.problem.dto.Problem.Response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(builderMethodName = "subjectiveBuilder")
public class SubjectiveProblemResponseDto extends ProblemResponseDto {
    private int aiGeneratedProblemId;
    private String problemName;
    private String problemCommentary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
