package com.app.domain.categorizedproblem.repository;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CategorizedProblemRepository extends JpaRepository<CategorizedProblem, Long> {
    boolean existsByCategoryCategoryIdAndProblemProblemId(Long categoryId, Long problemId);

    boolean existsByProblemProblemId(Long problemId);

    @Query(value = "SELECT cp FROM CategorizedProblem cp JOIN FETCH cp.problem p JOIN FETCH cp.category c WHERE c.categoryId = :categoryId",
        countQuery = "SELECT count(cp) FROM CategorizedProblem cp WHERE cp.category.categoryId = :categoryId")
    Page<CategorizedProblem> findByCategoryCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    List<CategorizedProblem> findByCategoryCategoryId(Long categoryId);
}
