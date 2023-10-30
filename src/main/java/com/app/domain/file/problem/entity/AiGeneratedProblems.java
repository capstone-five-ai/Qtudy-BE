package com.app.domain.file.problem.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGeneratedProblems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aiGeneratedProblemId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private ProblemFiles problemFiles;




    @Column(name = "problem_name", length = 100)
    private String problemName;

    @Column(name = "problem_content", length = 300, nullable = true)
    private String problemContent;

    @Column(name = "problem_user_answer",length = 100, nullable = true)
    private String problemUserAnswer;

    @Lob // TEXT 형식 변환
    @Column(name = "problem_answer")
    private String problemAnswer;


}
