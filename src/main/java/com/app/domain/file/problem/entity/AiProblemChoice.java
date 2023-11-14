package com.app.domain.file.problem.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiProblemChoice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_problem_choice_id")
    private int aiProblemChoiceId;//

    @ManyToOne
    @JoinColumn(name = "ai_generated_problem_id")
    private AiGeneratedProblems aiGeneratedProblems;


    @Column(name = "ai_problem_choice_content", length = 100,nullable = false)
    private String aiProblemChoiceContent;



}
