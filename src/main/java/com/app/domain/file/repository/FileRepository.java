package com.app.domain.file.repository;

import com.app.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {


    Optional<File> findByFileId(long FileId);

    Optional<String> findFileKeyByFileId(long fileId);

    Optional<File> findByFileNameAndDtype(String fileName, String type);

}
