package com.app.domain.problem.aigeneratedproblem.repository;

import com.app.domain.member.entity.Member;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemFileRepository extends JpaRepository<ProblemFile, Integer> {

    Optional<ProblemFile> findByFileId(int FileId);

    default ProblemFile getByFileId(int fileId) {
        return findByFileId(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));
    }

    Optional<ProblemFile> findByFileName(String FileName);

    Page<ProblemFile> findAllByMemberOrderByCreateTimeDesc(Member member, Pageable pageable);



}
