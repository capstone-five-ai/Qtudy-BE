package com.app.domain.problem.aigeneratedproblem.repository;

import com.app.domain.member.entity.Member;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemFileRepository extends JpaRepository<ProblemFile, Integer> {

    ProblemFile findByFileId(int FileId);

    Optional<ProblemFile> findByFileName(String FileName);

    Page<ProblemFile> findAllByMember(Member member, Pageable pageable);



}
