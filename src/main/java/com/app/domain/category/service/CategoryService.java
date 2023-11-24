package com.app.domain.category.service;

import com.app.domain.category.entity.Category;
import com.app.domain.category.repository.CategoryRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final MemberService memberService;

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category, HttpServletRequest httpServletRequest) {
        Member member = memberService.getLoginMember(httpServletRequest);
        category.updateMember(member);

        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long categoryId){
        Category preCategory = findVerifiedCategoryByCategoryId(categoryId);
        Optional.ofNullable(category.getCategoryName())
                .ifPresent(categoryName -> preCategory.updateCategoryName(categoryName));

        return categoryRepository.save(preCategory);
    }

    public void deleteCategory(Long categoryId){
        Category category = findVerifiedCategoryByCategoryId(categoryId);

        categoryRepository.deleteById(categoryId);
    }
    private Category findVerifiedCategoryByCategoryId(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_EXISTS));
    }
}
