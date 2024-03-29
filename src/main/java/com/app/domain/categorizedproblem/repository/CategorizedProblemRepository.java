package com.app.domain.categorizedproblem.repository;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorizedProblemRepository extends JpaRepository<CategorizedProblem, Long> {
    boolean existsByCategoryCategoryIdAndProblemProblemId(Long categoryId, Long problemId);

    boolean existsByProblemProblemId(Long problemId);

    Page<CategorizedProblem> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    List<CategorizedProblem> findByCategoryCategoryId(Long categoryId);
}
