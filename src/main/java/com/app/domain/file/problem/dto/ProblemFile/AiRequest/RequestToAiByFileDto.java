package com.app.domain.file.problem.dto.ProblemFile.AiRequest;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
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
public class RequestToAiByFileDto {
    private MultipartFile file;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;



}
