package com.app.domain.file.repository;

import com.app.global.config.ENUM.DType;
import com.app.domain.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<Files, Integer> {

    List<Files> findByMemberIdAndDtype(String memberId, DType dtype);
    List<Files> findByMemberIdAndFileNameContains(String memberId, String FileName);

    Optional<Files> findByFileId(int FileId);

    Optional<String> findFileKeyByFileId(int fileId);

}
