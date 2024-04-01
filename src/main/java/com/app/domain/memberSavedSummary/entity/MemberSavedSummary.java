package com.app.domain.memberSavedSummary.entity;

import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@SuperBuilder
@Builder
public class MemberSavedSummary extends BaseEntity {
    @Id
    @Column(name = "MEMBER_SAVED_SUMMARY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSavedSummaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "SUMMARY_TITLE")
    private String summaryTitle;

    @Column(name = "SUMMARY_CONTENT", columnDefinition = "TEXT")
    private String summaryContent;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateSummaryTitle(String summaryTitle) {
        this.summaryTitle = summaryTitle;
    }

    public void updateSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
}
