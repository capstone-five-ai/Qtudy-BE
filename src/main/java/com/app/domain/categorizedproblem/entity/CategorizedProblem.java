package com.app.domain.categorizedproblem.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.problem.entity.Problem;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Builder
public class CategorizedProblem extends BaseEntity {

    protected CategorizedProblem(){super();}
    @Id
    @Column(name = "CATEGORIZED_PROBLEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorizedProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Problem problem;

    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedProblems().add(this);
    }

}
