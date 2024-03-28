package com.app.domain.categorizedsummary.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
//@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategorizedSummary extends BaseEntity {
    protected CategorizedSummary(){super();}
    @Id
    @Column(name = "CATEGORIZED_SUMMARY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorizedSummaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_SAVED_SUMMARYS_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MemberSavedSummary memberSavedSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AI_GENERATED_SUMMARY_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AiGeneratedSummary aiGeneratedSummary;


    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedSummaries().add(this);
    }
}
