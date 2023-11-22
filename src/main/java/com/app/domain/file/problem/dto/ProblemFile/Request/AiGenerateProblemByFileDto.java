package com.app.domain.file.problem.dto.ProblemFile.Request;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.problem.dto.ProblemFile.AiRequest.RequestToAiByTextDto;
import com.app.domain.file.problem.dto.ProblemFile.AiRequest.TypeConvertDto;
import com.app.domain.file.problem.entity.ProblemFiles;
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
    private MultipartFile file;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;

    public RequestToAiByTextDto toTextDto(String text) {
        RequestToAiByTextDto textDto = new RequestToAiByTextDto();
        textDto.setText(text); // Assuming you have a method to convert MultipartFile to text
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        return textDto;
    }

    public TypeConvertDto toTextDto2(){
        TypeConvertDto textDto = new TypeConvertDto();
        textDto.setType(this.type);
        textDto.setAmount(this.amount);
        textDto.setDifficulty(this.difficulty);
        return textDto;
    }

}
