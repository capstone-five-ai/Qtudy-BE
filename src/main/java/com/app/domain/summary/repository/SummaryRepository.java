package com.app.domain.summary.repository;

import com.app.domain.summary.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {

}
