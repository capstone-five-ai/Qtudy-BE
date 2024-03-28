package com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request;

import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.AiRequest.RequestProblemToAiByTextDto;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;

    private String fileName;


    public RequestProblemToAiByTextDto toTextDto(String pdfText) {
        RequestProblemToAiByTextDto textDto = new RequestProblemToAiByTextDto();
        textDto.setText(pdfText); // Assuming you have a method to convert MultipartFile to text
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        return textDto;
    }

}
