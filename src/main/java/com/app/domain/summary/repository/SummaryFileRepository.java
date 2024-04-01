package com.app.domain.summary.repository;

import com.app.domain.file.entity.File;
import com.app.domain.member.entity.Member;
import com.app.domain.problem.entity.ProblemFile;
import com.app.domain.summary.entity.SummaryFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SummaryFileRepository extends JpaRepository<SummaryFile,Integer> {



    SummaryFile findByFileId(int FileId);

    Optional<SummaryFile> findByFileName(String FileName);

    Page<SummaryFile> findAllByMember(Member member, Pageable pageable);
}
