package com.app.domain.summary.entity;


import com.app.global.config.ENUM.ProblemType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGeneratedSummarys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="AI_GENERATED_SUMMARY_ID")
    private int aiGeneratedSummaryId;

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private SummaryFiles summaryFiles;


    @Column(name = "SUMMARY_TITLE", nullable = false)
    private String summaryTitle;


    @Lob // TEXT 형식 변환
    @Column(name = "SUMMARY_CONTENT", nullable = false)
    private String summaryContent;


    @Column(name = "PROBLEM_TYPE",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;

}
