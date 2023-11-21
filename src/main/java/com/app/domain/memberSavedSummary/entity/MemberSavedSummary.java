package com.app.domain.memberSavedSummary.entity;

import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSavedSummary extends BaseEntity {
    @Id
    @Column(name = "MEMBER_SAVED_SUMMARYS_ID")
    private Long memberSavedSummarysId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "SUMMARY_TITLE")
    private String summaryTitle;

    @Column(name = "SUMMARY_CONTENT", columnDefinition = "TEXT")
    private String summaryContent;
}
