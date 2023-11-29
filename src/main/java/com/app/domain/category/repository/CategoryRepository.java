package com.app.domain.category.repository;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByMemberMemberIdAndCategoryNameAndCategoryType(Long memberId, String categoryName, CategoryType categoryType);

    Page<Category> findByMemberMemberIdAndCategoryType(Long memberId, CategoryType categoryType, Pageable pageable);
}
