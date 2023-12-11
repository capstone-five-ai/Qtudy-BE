package com.app.domain.problem.dto.Problem.Response;

import com.app.domain.problem.dto.ProblemFile.Response.AiGeneratedProblemList;
import com.app.domain.problem.dto.ProblemFile.Response.AiGeneratedProblemResponseDto;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Setter
@Builder
public class ProblemResponseDto {
    @JsonProperty("isWriter")
    private boolean isWriter;
    private List<AiGeneratedProblemList> problems;



    public static ProblemResponseDto ConvertToProblem(boolean isWriter, List<AiGeneratedProblemList> aiGeneratedProblemList){
        return ProblemResponseDto.builder()
                .isWriter(isWriter)
                .problems(aiGeneratedProblemList)
                .build();
    }

    // Manual getter for isWriter field
    public boolean getIsWriter() {
        return isWriter;
    }

    // Lombok-generated getter for problems field
    public List<AiGeneratedProblemList> getProblems() {
        return problems;
    }
}


