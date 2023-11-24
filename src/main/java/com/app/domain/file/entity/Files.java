package com.app.domain.file.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.common.BaseTimeEntity;
import com.app.global.config.ENUM.DType;
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
        name = "FILES",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_id_file_name", columnNames = {"MEMBER_ID", "FILE_NAME"})
        }
)
public class Files extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private int fileId;




    @Column(name = "MEMBER_ID") //추후에 Members 엔티티와 연결
    private String memberId;

    @Column(name = "FILE_NAME", length = 100)
    private String fileName;

    @Column(name = "FILE_KEY", length = 100, unique = true)
    private String fileKey;


    @Column(name = "DTYPE")
    @Enumerated(EnumType.STRING)
    private DType dtype;


}
