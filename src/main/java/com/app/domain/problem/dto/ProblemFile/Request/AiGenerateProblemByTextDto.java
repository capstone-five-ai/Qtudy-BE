package com.app.domain.problem.dto.ProblemFile.Request;

import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemType;
import com.app.domain.problem.dto.ProblemFile.AiRequest.TypeConvertDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemByTextDto {
    private String text;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;

    private String fileName;

    public TypeConvertDto toTextDto2(){
        TypeConvertDto textDto = new TypeConvertDto();
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        textDto.setFileName(fileName);
        return textDto;
    }

}
