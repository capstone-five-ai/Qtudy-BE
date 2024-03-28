package com.app.domain.problem.aigeneratedproblem.entity;

import com.app.domain.file.entity.File;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
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
public class ProblemFile extends File {


    @OneToMany(mappedBy = "problemFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiGeneratedProblem> aiQuestions;





    @Column(name = "PROBLEM_DIFFICULTY",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemDifficulty problemDifficulty;

    @Column(name = "PROBLEM_AMOUNT",nullable = false)
    @Enumerated(EnumType.STRING)
    private Amount problemAmount;

    @Column(name = "PROBLEM_TYPE",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;
}
