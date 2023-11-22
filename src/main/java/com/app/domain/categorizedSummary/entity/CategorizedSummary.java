package com.app.domain.categorizedSummary.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategorizedSummary extends BaseEntity {
    @Id
    @Column(name = "CATEGORIZED_SUMMARY_ID")
    private Long categorizedSummaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_SAVED_SUMMARYS_ID", nullable = false)
    private MemberSavedSummary memberSavedSummary;

    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedSummaries().add(this);
    }
}
