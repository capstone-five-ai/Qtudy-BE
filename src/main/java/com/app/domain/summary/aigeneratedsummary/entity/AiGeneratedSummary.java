package com.app.domain.summary.aigeneratedsummary.entity;


import com.app.domain.summary.entity.Summary;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AiGeneratedSummary extends Summary {

    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private SummaryFile summaryFile;
}
