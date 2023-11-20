package com.app.domain.file.problem.dto.Problem.Response;

import com.app.domain.file.problem.entity.AiProblemChoice;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FileNameResponseDto {
    private int aiGeneratedProblemId;
    private String problemName;
    private List<AiProblemChoice> problemChoices; // Assuming AiProblemChoice has appropriate DTO or can be mapped directly
    private String problemAnswer;
    private String problemCommentary;
}
