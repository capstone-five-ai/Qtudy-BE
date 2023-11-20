package com.app.domain.file.problem.dto.ProblemFile.AiRequest;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TypeConvertDto {
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;



}
