package com.app.domain.categorizedProblem.repository;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedProblemRepository extends JpaRepository<CategorizedProblem, Long> {
    boolean existsByCategoryCategoryIdAndMemberSavedProblemMemberSavedProblemId(Long categoryId, Long memberSavedProblemId);

    boolean existsByCategoryCategoryIdAndAiGeneratedProblemAiGeneratedProblemId(Long categoryId, Integer aiGeneratedProblemId);

    boolean existsByMemberSavedProblemMemberSavedProblemId(Long memberSavedProblemId);

    Page<CategorizedProblem> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
