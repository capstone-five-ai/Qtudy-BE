package com.app.domain.summary.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
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
public abstract class Summary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SUMMARY_ID")
    private Long summaryId;

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

    public boolean isMemberSavedSummary() {
        return this instanceof MemberSavedSummary;
    }
}
