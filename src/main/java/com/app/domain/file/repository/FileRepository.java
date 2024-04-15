package com.app.domain.file.repository;

import com.app.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {


    Optional<File> findByFileId(int FileId);

    Optional<String> findFileKeyByFileId(int fileId);

    Optional<File> findByFileNameAndDtype(String fileName, String type);

}
