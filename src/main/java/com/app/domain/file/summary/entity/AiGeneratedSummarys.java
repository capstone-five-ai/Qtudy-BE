package com.app.domain.file.summary.entity;


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
    @Column(name="ai_generated_summary_id")
    private int aiGeneratedSummaryId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private SummaryFiles summaryFiles;


    @Column(name = "summary_title", nullable = false)
    private String summaryTitle;


    @Lob // TEXT 형식 변환
    @Column(name = "summary_content", nullable = false)
    private String summaryContent;

}
