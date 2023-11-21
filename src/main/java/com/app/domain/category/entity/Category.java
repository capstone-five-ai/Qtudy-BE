package com.app.domain.category.entity;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
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
public class Category extends BaseEntity {
    @Id
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategorizedSummary> categorizedSummaries;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategorizedProblem> categorizedProblems;
}
