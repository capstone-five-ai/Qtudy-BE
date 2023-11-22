package com.app.domain.memberSavedSummary.entity;

import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
