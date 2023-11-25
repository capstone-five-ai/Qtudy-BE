package com.app.domain.problem.repository;

import com.app.domain.problem.entity.ProblemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemFileRepository extends JpaRepository<ProblemFile, Integer> {

    ProblemFile findByMemberIdAndFileId(String MemberId, int FileId);

    Optional<ProblemFile> findByFileName(String FileName);

}
