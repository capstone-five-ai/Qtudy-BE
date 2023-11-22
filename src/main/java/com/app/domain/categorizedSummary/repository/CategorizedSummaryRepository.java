package com.app.domain.categorizedSummary.repository;

import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedSummaryRepository extends JpaRepository<CategorizedSummary, Long> {
}
