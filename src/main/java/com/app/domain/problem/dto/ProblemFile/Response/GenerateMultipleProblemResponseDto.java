package com.app.domain.problem.dto.ProblemFile.Response;

import com.app.global.config.ENUM.ProblemType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(builderMethodName = "multipleBuilder")
public class GenerateMultipleProblemResponseDto extends GenerateProblemResponseDto{
    private int aiGeneratedProblemId;
    private String problemName;
    private List<String> problemChoices; // Assuming AiProblemChoice has appropriate DTO or can be mapped directly
    private String problemAnswer;
    private String problemCommentary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
