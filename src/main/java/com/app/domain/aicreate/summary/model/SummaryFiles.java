package com.app.domain.aicreate.summary.model;

import com.app.domain.aicreate.common.ENUM.Amount;
import com.app.domain.aicreate.common.model.Files;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(schema = "Qtudy",name = "summaryfiles")
public class SummaryFiles extends Files {

    @OneToMany(mappedBy = "SummaryFiles")
    private List<AiGeneratedSummarys> aiQuestions;

    @Column(name = "amount")
    @Enumerated(EnumType.STRING)
    private Amount amount;
}
