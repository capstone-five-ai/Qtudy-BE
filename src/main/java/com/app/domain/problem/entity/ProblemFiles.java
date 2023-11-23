package com.app.domain.problem.entity;

import com.app.domain.file.entity.Files;
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
@SuperBuilder
public class ProblemFiles extends Files {


    @OneToMany(mappedBy = "problemFiles", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiGeneratedProblems> aiQuestions;





    @Column(name = "problem_difficulty",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemDifficulty problemDifficulty;

    @Column(name = "problem_amount",nullable = false)
    @Enumerated(EnumType.STRING)
    private Amount problemAmount;

    @Column(name = "problem_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;
}
