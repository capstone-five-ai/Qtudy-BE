package com.app.domain.memberSavedProblem.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import com.app.domain.memberSavedProblem.constant.ProblemType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@SuperBuilder
@Builder
public class MemberSavedProblem extends BaseEntity {

//    protected MemberSavedProblem(){
//        super();
//    }
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
    @Column(name = "CHOICE_CONTENT")
    private List<String> problemChoices;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateProblemName(String problemName) {
        this.problemName = problemName;
    }

    public void updateProblemAnswer(String problemAnswer) {
        this.problemAnswer = problemAnswer;
    }

    public void updateProblemCommentary(String problemCommentary) {
        this.problemCommentary = problemCommentary;
    }
}
