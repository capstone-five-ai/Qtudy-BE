package com.app.domain.memberSavedProblem.entity;

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
public class MemberSavedProblem extends BaseEntity {
    @Id
    @Column(name = "MEMBER_SAVED_PROBLEM_ID")
    private Long memberSavedProblemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "PROBLEM_NAME")
    private String problemName;

    @Column(name = "PROBLEM_ANSWER")
    private String problemAnswer;

    @Column(name = "PROBLEM_COMMENTARY", columnDefinition = "TEXT")
    private String problemCommentary;

    @ElementCollection
    @CollectionTable(name = "PROBLEM_CHOCIE", joinColumns = @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID"))
    @Column(name = "CHOICE_CONTETN")
    private List<String> problemChocies;

}
