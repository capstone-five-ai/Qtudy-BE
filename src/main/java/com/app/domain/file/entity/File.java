package com.app.domain.file.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
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
@DiscriminatorColumn(name = "dtype")
@Table( // MemberId와 fileName을 섞어 Unique 조건 생성
        name = "FILE",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_id_file_name", columnNames = {"MEMBER_ID", "FILE_NAME"})
        }
)
public abstract class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long fileId;

    //@Column(name = "MEMBER_ID") //추후에 Members 엔티티와 연결
    //private String memberId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "FILE_NAME", length = 100)
    private String fileName;

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;
    /*@Column(name = "FILE_KEY", length = 100, unique = true)
    private String fileKey;*/
}