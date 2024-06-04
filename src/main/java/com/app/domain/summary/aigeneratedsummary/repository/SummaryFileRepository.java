package com.app.domain.summary.aigeneratedsummary.repository;

import com.app.domain.member.entity.Member;
import com.app.domain.summary.aigeneratedsummary.entity.SummaryFile;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SummaryFileRepository extends JpaRepository<SummaryFile,Long> {



    Optional<SummaryFile> findByFileId(long FileId);

    default SummaryFile getByFileId(long fileId) {
        return findByFileId(fileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FILE));
    }

    Optional<SummaryFile> findByFileName(String FileName);

    Page<SummaryFile> findAllByMemberOrderByCreateTimeDesc(Member member, Pageable pageable);
}
