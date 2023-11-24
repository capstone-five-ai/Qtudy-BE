package com.app.domain.summary.repository;

import com.app.domain.summary.entity.AiGeneratedSummarys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiGeneratedSummarysRepository extends JpaRepository<AiGeneratedSummarys,Integer> {
}
