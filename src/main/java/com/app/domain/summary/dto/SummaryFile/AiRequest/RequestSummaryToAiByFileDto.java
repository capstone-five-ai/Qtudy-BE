package com.app.domain.summary.dto.SummaryFile.AiRequest;

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
public class RequestSummaryToAiByFileDto {
    private MultipartFile file;
    private ProblemType type;
    private Amount amount;
    private ProblemDifficulty difficulty;
    private String fileName;



}
