package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemFromAiDto { // 객관식 받아오는 DTO

    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;
}
