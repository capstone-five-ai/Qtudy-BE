package com.app.domain.summary.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public abstract class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SUMMARY_ID")
    private Integer summaryId;

    @Column(name = "SUMMARY_TITLE", length = 100, nullable = false)
    private String summaryTitle;

    @Column(name = "SUMMARY_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String summaryContent;

    public void updateSummaryTitle(String summaryTitle) {
        this.summaryTitle = summaryTitle;
    }

    public void updateSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
}
