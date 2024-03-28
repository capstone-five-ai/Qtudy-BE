package com.app.domain.summary.aigeneratedsummary.entity;

import com.app.domain.file.entity.File;
import com.app.global.config.ENUM.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SummaryFile extends File {

    @OneToMany(mappedBy = "summaryFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiGeneratedSummary> aiQuestions;

    @Column(name = "SUMMARY_AMOUNT", nullable = false)
    @Enumerated(EnumType.STRING)
    private Amount summaryAmount;
}
