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
public class UpdateProblemChoicesRequestDto {
    private int aiGeneratedProblemId;
    private List<String> problemChoices;
}
