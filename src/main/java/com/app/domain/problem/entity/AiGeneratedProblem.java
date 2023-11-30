package com.app.domain.problem.entity;

import com.app.domain.common.BaseEntity;
import com.app.global.config.ENUM.ProblemType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AiGeneratedProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AI_GENERATED_PROBLEM_ID")
    private Integer aiGeneratedProblemId;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private ProblemFile problemFile;




    @Column(name = "PROBLEM_NAME", length = 100,nullable = false)
    private String problemName;

    @ElementCollection
    private List<String> problemChoices;

    @Column(name = "PROBLEM_ANSWER", length = 300)
    private String problemAnswer;

    @Column(name = "PROBLEM_COMMENTARY", columnDefinition = "TEXT", nullable = false)
    private String problemCommentary;

    @Column(name = "PROBLEM_TYPE",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;


    public void updateProblemName(String problemName) {
        this.problemName = problemName;
    }

    public void updateProblemAnswer(String problemAnswer) {
        this.problemAnswer = problemAnswer;
    }

    public void updateProblemCommentary(String problemCommentary) {
        this.problemCommentary = problemCommentary;
    }
}
