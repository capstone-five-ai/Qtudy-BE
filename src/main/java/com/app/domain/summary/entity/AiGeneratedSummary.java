package com.app.domain.summary.entity;


import com.app.domain.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AiGeneratedSummary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="AI_GENERATED_SUMMARY_ID")
    private int aiGeneratedSummaryId;

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private SummaryFile summaryFile;


    @Column(name = "SUMMARY_TITLE", nullable = false)
    private String summaryTitle;


    @Lob // TEXT 형식 변환
    @Column(name = "SUMMARY_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String summaryContent;



}
