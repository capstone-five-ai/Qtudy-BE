package com.app.domain.memberSavedProblem.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import com.app.global.config.ENUM.ProblemType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@SuperBuilder
public class MemberSavedProblem extends BaseEntity {
    @Id
    @Column(name = "MEMBER_SAVED_PROBLEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ProblemType problemType;

    @ElementCollection
    @CollectionTable(name = "PROBLEM_CHOCIE", joinColumns = @JoinColumn(name = "MEMBER_SAVED_PROBLEM_ID"))
    @Column(name = "CHOICE_CONTETN")
    private List<String> problemChocies;

    public void updateMember(Member member) {
        this.member = member;
    }
}
