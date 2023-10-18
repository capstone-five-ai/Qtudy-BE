package com.app.domain.aicreate.summary.model;


import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Table(schema = "Qtudy",name = "aigeneratedsummarys")
public class AiGeneratedSummarys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aiGeneratedSummaryId;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private SummaryFiles summaryFiles;




    @Lob // TEXT 형식 변환
    @Column(name = "summarycontent")
    private String summaryContent;

}
