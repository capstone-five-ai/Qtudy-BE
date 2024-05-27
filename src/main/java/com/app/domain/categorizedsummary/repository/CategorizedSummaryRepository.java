package com.app.domain.categorizedsummary.repository;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedSummaryRepository extends JpaRepository<CategorizedSummary, Long> {
    boolean existsByCategoryCategoryIdAndMemberSavedSummaryMemberSavedSummaryId(Long categoryId, Long memberSavedSummaryId);

    boolean existsByCategoryCategoryIdAndAiGeneratedSummaryAiGeneratedSummaryId(Long categoryId, Integer aiGeneratedSummaryId);

    boolean existsByMemberSavedSummaryMemberSavedSummaryId(Long memberSavedSummaryId);

    Page<CategorizedSummary> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
