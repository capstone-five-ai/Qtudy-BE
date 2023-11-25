package com.app.domain.summary.repository;

import com.app.domain.summary.entity.AiGeneratedSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiGeneratedSummaryRepository extends JpaRepository<AiGeneratedSummary,Integer> {
}
