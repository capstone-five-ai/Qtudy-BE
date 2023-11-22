package com.app.domain.categorizedProblem.repository;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedProblemRepository extends JpaRepository<CategorizedProblem, Long> {
}
