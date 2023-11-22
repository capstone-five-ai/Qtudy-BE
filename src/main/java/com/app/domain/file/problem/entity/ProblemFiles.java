package com.app.domain.file.problem.entity;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.common.ENUM.ProblemDifficulty;
import com.app.domain.file.common.ENUM.ProblemType;
import com.app.domain.file.file.entity.Files;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
