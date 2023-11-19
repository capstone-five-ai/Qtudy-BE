package com.app.domain.file.problem.dto.ProblemChoice;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddChoiceDto {
    private int aiGeneratedProblemId;
    private String content;
}
