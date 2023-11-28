package com.app.domain.categorizedProblem.entity;

import com.app.domain.category.entity.Category;
import com.app.domain.common.BaseEntity;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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
    @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MemberSavedProblem memberSavedProblem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AI_GENERATED_PROBLEM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AiGeneratedProblems aiGeneratedProblems;

    @PrePersist
    @PreUpdate
    private void validateFk() {
        if (memberSavedProblem != null && aiGeneratedProblems != null) {
            throw new BusinessException(ErrorCode.FK_BOTH_EXISTS);
        }
        else if(memberSavedProblem == null && aiGeneratedProblems == null){
            throw new BusinessException(ErrorCode.FK_NOT_EXISTS);
        }
    }

    public void updateCategory(Category category){
        this.category = category;
        category.getCategorizedProblems().add(this);
    }

}
