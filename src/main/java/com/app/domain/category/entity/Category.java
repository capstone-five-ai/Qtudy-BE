package com.app.domain.category.entity;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.category.contsant.CategoryType;
import com.app.domain.common.BaseEntity;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryType categoryType;

    @OneToMany(mappedBy = "category")
    private List<CategorizedSummary> categorizedSummaries;

    @OneToMany(mappedBy = "category")
    private List<CategorizedProblem> categorizedProblems;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
