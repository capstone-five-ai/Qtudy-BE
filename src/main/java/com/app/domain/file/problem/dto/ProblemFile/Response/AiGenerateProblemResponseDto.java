package com.app.domain.file.problem.dto.ProblemFile.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemResponseDto {

    private String problemName;
    private List<String> problemChoices;
    private String problemCommentary;
}
