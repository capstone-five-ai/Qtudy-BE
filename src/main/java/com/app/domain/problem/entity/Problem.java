package com.app.domain.problem.entity;

import com.app.domain.common.BaseEntity;
import com.app.global.config.ENUM.ProblemType;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public abstract class Problem extends BaseEntity {

    @Id
    @Column(name = "PROBLEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    @Column(name = "PROBLEM_NAME", length = 100, nullable = false)
    private String problemName;

    @Column(name = "PROBLEM_ANSWER", length = 300)
    private String problemAnswer;

    @Column(name = "PROBLEM_COMMENTARY", columnDefinition = "TEXT", nullable = false)
    private String problemCommentary;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROBLEM_TYPE", nullable = false, length = 10)
    private ProblemType problemType;

    @ElementCollection
    @CollectionTable(name = "PROBLEM_CHOICE", joinColumns = @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID"))
    @Column(name = "CHOICE_CONTENT", nullable = false)
    private List<String> problemChoices;

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
