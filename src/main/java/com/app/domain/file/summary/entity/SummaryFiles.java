package com.app.domain.file.summary.entity;

import com.app.domain.file.common.ENUM.Amount;
import com.app.domain.file.file.entity.Files;
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
public class SummaryFiles extends Files {

    @OneToMany(mappedBy = "summaryFiles")
    private List<AiGeneratedSummarys> aiQuestions;

    @Column(name = "summary_amount", nullable = false)
    @Enumerated(EnumType.STRING)
    private Amount summaryAmount;
}
