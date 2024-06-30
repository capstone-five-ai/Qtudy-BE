package com.app.domain.categorizedsummary.repository;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategorizedSummaryRepository extends JpaRepository<CategorizedSummary, Long> {
    boolean existsByCategoryCategoryIdAndSummarySummaryId(Long categoryId, Long summaryId);

    boolean existsBySummarySummaryId(Long summaryId);

    @Query(value = "SELECT cs FROM CategorizedSummary cs JOIN FETCH cs.summary s JOIN FETCH cs.category c WHERE c.categoryId = :categoryId",
        countQuery = "SELECT count(cs) FROM CategorizedSummary cs WHERE cs.category.categoryId = :categoryId")
    Page<CategorizedSummary> findByCategoryCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
