package com.app.domain.file.problem.entity;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.Difficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.common.entity.Files;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProblemFiles extends Files {

    @OneToMany(mappedBy = "problemFiles")
    private List<AiGeneratedProblems> aiQuestions;





    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "problem_amount")
    @Enumerated(EnumType.STRING)
    private Amount problemAmount;

    @Column(name = "problem_type")
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;
}
