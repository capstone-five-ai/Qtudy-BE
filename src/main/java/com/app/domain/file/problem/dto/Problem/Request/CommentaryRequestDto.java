package com.app.domain.file.problem.dto.Problem.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentaryRequestDto {
    private int aiGeneratedProblemId;
    private String commentary;
}
