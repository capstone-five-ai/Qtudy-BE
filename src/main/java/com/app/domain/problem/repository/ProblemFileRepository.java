package com.app.domain.problem.repository;

import com.app.domain.file.entity.File;
import com.app.domain.member.entity.Member;
import com.app.domain.problem.entity.ProblemFile;
import com.app.global.config.ENUM.DType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemFileRepository extends JpaRepository<ProblemFile, Integer> {

    ProblemFile findByFileId(int FileId);

    Optional<ProblemFile> findByFileName(String FileName);

    Page<ProblemFile> findAllByMemberId(Member memberId, Pageable pageable);



}
