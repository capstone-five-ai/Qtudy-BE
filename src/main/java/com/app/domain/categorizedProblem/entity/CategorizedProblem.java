package com.app.domain.categorizedProblem.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter

@SuperBuilder
public class CategorizedProblem extends BaseEntity {
    @Id
    @Column(name = "CATEGORIZED_PROBLEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorizedProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID", nullable = false)
    private MemberSavedProblem memberSavedProblem;

    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedProblems().add(this);
    }

}
