package com.app.domain.file.problem.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGeneratedProblems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_generated_problem_id")
    private int aiGeneratedProblemId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private ProblemFiles problemFiles;




    @Column(name = "problem_name", length = 100,nullable = false)
    private String problemName;

    @OneToMany(mappedBy = "aiGeneratedProblems", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<AiProblemChoice> problemChoices;

    @Column(name = "problem_answer", length = 300)
    private String problemAnswer;

    @Column(name = "problem_commentary", columnDefinition = "TEXT", nullable = false)
    private String problemCommentary;



}
