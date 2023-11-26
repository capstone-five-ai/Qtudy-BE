package com.app.domain.problem.dto.ProblemFile.Response;

import com.app.global.config.ENUM.ProblemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(builderMethodName = "subjectiveBuilder")
public class GenerateSubjectiveProblemResponseDto extends GenerateProblemResponseDto{
    private int aiGeneratedProblemId;
    private String problemName;
    private String problemCommentary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
