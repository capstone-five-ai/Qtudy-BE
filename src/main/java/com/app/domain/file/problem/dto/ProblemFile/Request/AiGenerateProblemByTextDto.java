package com.app.domain.file.problem.dto.ProblemFile.Request;

import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.problem.dto.ProblemFile.AiRequest.TypeConvertDto;
import com.app.domain.file.problem.entity.ProblemFiles;
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

    public TypeConvertDto toTextDto2(){
        TypeConvertDto textDto = new TypeConvertDto();
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        return textDto;
    }

}
