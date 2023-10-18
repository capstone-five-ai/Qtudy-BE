package com.app.domain.aicreate.problem.model;

import com.app.domain.aicreate.common.ENUM.Amount;
import com.app.domain.aicreate.common.ENUM.Difficulty;
import com.app.domain.aicreate.common.ENUM.ProblemType;
import com.app.domain.aicreate.common.model.Files;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "Qtudy",name = "problemfiles")
public class ProblemFiles extends Files {

    @OneToMany(mappedBy = "problemFiles")
    private List<AiGeneratedProblems> aiQuestions;




    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "amount")
    @Enumerated(EnumType.STRING)
    private Amount amount;

    @Column(name = "Problemtype")
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;
}
