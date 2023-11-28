package com.app.domain.problem.dto.ProblemFile.Request;

import com.app.domain.problem.dto.ProblemFile.AiRequest.RequestProblemToAiByTextDto;
import com.app.domain.problem.dto.ProblemFile.AiRequest.TypeConvertProblemDto;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemByFileDto {
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;
    private String fileName;

    public RequestProblemToAiByTextDto toTextDto(String text) {
        RequestProblemToAiByTextDto textDto = new RequestProblemToAiByTextDto();
        textDto.setText(text); // Assuming you have a method to convert MultipartFile to text
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        textDto.setFileName(this.fileName);
        return textDto;
    }

    public TypeConvertProblemDto toTextDto2(){
        TypeConvertProblemDto textDto = new TypeConvertProblemDto();
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        textDto.setFileName(this.fileName);
        return textDto;
    }

}
