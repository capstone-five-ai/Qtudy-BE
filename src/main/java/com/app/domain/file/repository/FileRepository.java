package com.app.domain.file.repository;

import com.app.global.config.ENUM.DType;
import com.app.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {


    Optional<File> findByFileId(int FileId);

    Optional<String> findFileKeyByFileId(int fileId);

}
