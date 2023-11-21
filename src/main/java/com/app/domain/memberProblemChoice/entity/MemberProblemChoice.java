package com.app.domain.memberProblemChoice.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProblemChoice extends BaseEntity {
    @Id
    @Column(name = "AI_PROBLEM_CHOICE_ID")
    private Integer aiProblemChoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID", nullable = false)
    private MemberSavedProblem memberSavedProblem;

    @Column(name = "AI_PROBLEM_CHOICE_CONTENT")
    private String aiProblemChoiceContent;

    public void updateMemberSavedProblem(MemberSavedProblem memberSavedProblem){
        this.memberSavedProblem = memberSavedProblem;
        memberSavedProblem.getProblemChoices().add(this);
    }
}
