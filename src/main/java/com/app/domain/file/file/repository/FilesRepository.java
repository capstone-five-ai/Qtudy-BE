package com.app.domain.file.file.repository;

import com.app.domain.file.file.entity.Files;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<Files, Integer> {

    List<String> findFileNameByMemberId(String memberId);
    List<String> findFileNameByMemberIdAndFileNameContains(String memberId, String FileName);

    Optional<Files> findFileNameByMemberIdAndFileName(String memberId, String FileName);

    Optional<String> findFileKeyByMemberIdAndFileName(String memberId, String FileName);

}
