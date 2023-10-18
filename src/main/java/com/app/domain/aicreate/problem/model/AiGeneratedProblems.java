package com.app.domain.aicreate.problem.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;


@Entity
@Table(schema = "Qtudy",name = "aigeneratedproblem")
public class AiGeneratedProblems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aiGeneratedProblemId;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private ProblemFiles problemFiles;




    @Column(name = "problemname", length = 100)
    private String problemName;

    @Column(name = "problemcontent", length = 300, nullable = true)
    private String problemContent;

    @Lob // TEXT 형식 변환
    @Column(name = "problemanswer")
    private String problemAnswer;


}
