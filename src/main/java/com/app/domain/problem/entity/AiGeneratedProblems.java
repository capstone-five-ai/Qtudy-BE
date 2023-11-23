package com.app.domain.problem.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
public class AiGeneratedProblems extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_generated_problem_id")
    private int aiGeneratedProblemId;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "file_id")
    private ProblemFiles problemFiles;




    @Column(name = "problem_name", length = 100,nullable = false)
    private String problemName;

    @ElementCollection
    private List<String> problemChoices;

    @Column(name = "problem_answer", length = 300)
    private String problemAnswer;

    @Column(name = "problem_commentary", columnDefinition = "TEXT", nullable = false)
    private String problemCommentary;

}
