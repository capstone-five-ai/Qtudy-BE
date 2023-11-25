package com.app.domain.problem.dto.ProblemFile.AiRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemToAiDto { // 객관식 받아오는 DTO

    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;
}
