package com.app.domain.aicreate.common.model;

import com.app.domain.aicreate.common.ENUM.DType;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "Qtudy",name = "files")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;




    @Column(name = "memberid")
    private int memberId;

    @Column(name = "filename", length = 100)
    private String fileName;

    @Column(name = "fileurl", length = 100)
    private String fileUrl;

    @Column(name = "filedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fileDate;

    @Column(name = "dtype")
    @Enumerated(EnumType.STRING)
    private DType dtype;
}
