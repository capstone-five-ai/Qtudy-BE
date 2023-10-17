package com.app.domain.aicreate.dto;

import com.app.domain.aicreate.ENUM.Difficulty;
import com.app.domain.aicreate.ENUM.Amount;
import com.app.domain.aicreate.ENUM.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QuestionDto {
    private String text;
    private QuestionType type;
    private Amount amount;
    private Difficulty difficulty;

}
