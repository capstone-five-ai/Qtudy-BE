package com.app.domain.file.problem.dto.ProblemFile.Request;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.DType;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.problem.entity.ProblemFiles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeleteProblemFileDto {
    private int fileId;
}
