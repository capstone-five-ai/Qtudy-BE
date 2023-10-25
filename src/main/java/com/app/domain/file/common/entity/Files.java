package com.app.domain.file.common.entity;

import com.app.domain.file.common.ENUM.DType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id")
    private int fileId;




    @Column(name = "member_id") //추후에 Members 엔티티와 연결
    private int memberId;

    @Column(name = "file_name", length = 100)
    private String fileName;

    @Column(name = "file_url", length = 100)
    private String fileUrl;

    @Column(name = "file_date")
    private LocalDateTime fileDate;

    @Column(name = "dtype")
    @Enumerated(EnumType.STRING)
    private DType dtype;




    @PrePersist
    public void prePersist() { //시간 자동 설정 (데이터베이스에 변환 될 때)
        if (fileDate == null) {
            fileDate = LocalDateTime.now();
        }
    }
}
