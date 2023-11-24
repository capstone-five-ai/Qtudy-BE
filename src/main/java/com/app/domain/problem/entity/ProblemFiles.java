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
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProblemFiles extends Files {


    @OneToMany(mappedBy = "problemFiles", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiGeneratedProblems> aiQuestions;





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
