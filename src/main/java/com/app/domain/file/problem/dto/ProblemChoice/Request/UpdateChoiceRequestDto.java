package com.app.domain.file.problem.dto.ProblemChoice.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateChoiceRequestDto {
    private int aiProblemChoiceId;
    private String newContent;
}
