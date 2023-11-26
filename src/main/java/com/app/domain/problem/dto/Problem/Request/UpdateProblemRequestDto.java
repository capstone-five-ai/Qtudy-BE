package com.app.domain.problem.dto.Problem.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateProblemRequestDto {
    private String problemName;
    private List<String> problemChoices;
    private String problemAnswer;
    private String problemCommentary;
}
