package com.app.domain.file.file.entity;

import com.app.domain.file.common.ENUM.DType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Files {

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

    @Column(name = "file_date")
    @CreationTimestamp
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
