package com.app.domain.summary.aigeneratedsummary.repository;

import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiGeneratedSummaryRepository extends JpaRepository<AiGeneratedSummary,Long> {

    Optional<AiGeneratedSummary> findBySummaryFile_FileId(long fileId);
}
