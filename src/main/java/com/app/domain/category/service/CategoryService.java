package com.app.domain.category.service;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.entity.Category;
import com.app.domain.category.repository.CategoryRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

        if (isCategoryNameUsedInSameMeber(member.getMemberId(),category.getCategoryName(), category.getCategoryType())) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
        }

        category.updateMember(member);



        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long categoryId){
        Category preCategory = findVerifiedCategoryByCategoryId(categoryId);

        if (isCategoryNameUsedInSameMeber(preCategory.getMember().getMemberId(), category.getCategoryName(), category.getCategoryType())) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
        }

        Optional.ofNullable(category.getCategoryName())
                .ifPresent(categoryName -> preCategory.updateCategoryName(categoryName));

        return categoryRepository.save(preCategory);
    }

    @Transactional(readOnly = true)
    public Page<Category> findCategoriesByMemberIdAndType(int page, int size, CategoryType categoryType, HttpServletRequest httpServletRequest) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("categoryName"));
        Member member = memberService.getLoginMember(httpServletRequest);

        return categoryRepository.findByMemberMemberIdAndCategoryType(member.getMemberId(), categoryType, pageRequest);
    }

    public void deleteCategory(Long categoryId){
        Category category = findVerifiedCategoryByCategoryId(categoryId);

        categoryRepository.deleteById(categoryId);
    }

    private boolean isCategoryNameUsedInSameMeber(Long memberId, String categoryName, CategoryType categoryType) {
        return categoryRepository.existsByMemberMemberIdAndCategoryNameAndCategoryType(memberId, categoryName, categoryType);
    }

    public Category findVerifiedCategoryByCategoryId(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_EXISTS));
    }
}
