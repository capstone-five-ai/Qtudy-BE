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
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Files extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;




    @Column(name = "member_id") //추후에 Members 엔티티와 연결
    private String memberId;

    @Column(name = "file_name", length = 100, unique = true)
    private String fileName;

    @Column(name = "file_key", length = 100, unique = true)
    private String fileKey;


    @Column(name = "dtype")
    @Enumerated(EnumType.STRING)
    private DType dtype;


}
