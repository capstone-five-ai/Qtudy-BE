package com.app.domain.categorizedsummary.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.summary.entity.Summary;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "SUMMARY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Summary summary;

    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedSummaries().add(this);
    }
}
