package com.app.domain.file.file.repository;

import com.app.domain.file.file.entity.Files;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<Files, Integer> {

    List<Files> findByMemberId(String memberId);
    List<Files> findByMemberIdAndFileNameContains(String memberId, String FileName);

    Optional<Files> findByFileId(int FileId);

    Optional<String> findFileKeyByFileId(int fileId);

}
