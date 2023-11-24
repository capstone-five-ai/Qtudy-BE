package com.app.domain.problem.dto.ProblemFile.Request;

import com.app.domain.problem.dto.ProblemFile.AiRequest.RequestToAiByTextDto;
import com.app.domain.problem.dto.ProblemFile.AiRequest.TypeConvertDto;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AiGenerateProblemByFileDto {
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;
    private String fileName;

    public RequestToAiByTextDto toTextDto(String text) {
        RequestToAiByTextDto textDto = new RequestToAiByTextDto();
        textDto.setText(text); // Assuming you have a method to convert MultipartFile to text
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        textDto.setFileName(this.fileName);
        return textDto;
    }

    public TypeConvertDto toTextDto2(){
        TypeConvertDto textDto = new TypeConvertDto();
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        textDto.setFileName(this.fileName);
        return textDto;
    }

}
