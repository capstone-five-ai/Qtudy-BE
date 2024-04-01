package com.app.domain.file.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import com.app.domain.problem.entity.ProblemFile;
import com.app.global.config.ENUM.DType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Table( // MemberId와 fileName을 섞어 Unique 조건 생성
        name = "FILE",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_id_file_name", columnNames = {"MEMBER_ID", "FILE_NAME"})
        }
)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private int fileId;




    //@Column(name = "MEMBER_ID") //추후에 Members 엔티티와 연결
    //private String memberId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "FILE_NAME", length = 100)
    private String fileName;

    /*@Column(name = "FILE_KEY", length = 100, unique = true)
    private String fileKey;*/


    @Column(name = "DTYPE")
    @Enumerated(EnumType.STRING)
    private DType dtype;


}
