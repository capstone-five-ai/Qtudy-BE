package com.app.domain.category.controller;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.categorizedproblem.service.CategorizedProblemService;
import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.categorizedsummary.service.CategorizedSummaryService;
import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.entity.Category;
import com.app.domain.category.mapper.CategoryMapper;
import com.app.domain.category.service.CategoryService;
import com.app.domain.common.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final CategorizedProblemService categorizedProblemService;

    private final CategorizedSummaryService categorizedSummaryService;

    private final CategoryMapper categoryMapper;

    @PostMapping("/new")
    public ResponseEntity createCategory(@Valid @RequestBody CategoryDto.RequestDto categoryReqeustDto,
                                         HttpServletRequest httpServletRequest) {
        Category category =
                categoryService.createCategory(categoryMapper.categoryRequestDtoToCategory(categoryReqeustDto), httpServletRequest);

        CategoryDto.Response response = categoryMapper.categoryToCategoryResponse(category);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable @Positive Long categoryId,
                                         @Valid @RequestBody CategoryDto.RequestDto categoryReqeustDto) {
        Category category =
                categoryService.updateCategory(categoryMapper.categoryRequestDtoToCategory(categoryReqeustDto), categoryId);

        CategoryDto.Response response = categoryMapper.categoryToCategoryResponse(category);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable @Positive Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity getCategories(@Positive @RequestParam(value = "page",defaultValue = "1") int page,
                                        @Positive @RequestParam(value = "size", defaultValue = "10") int size,
                                        @RequestParam(value = "categoryType") CategoryType categoryType,
                                        HttpServletRequest httpServletRequest){
        Page<Category> pageCategories = categoryService.findCategoriesByMemberIdAndType(page-1, size, categoryType, httpServletRequest);

        List<Category> categoryList = pageCategories.getContent();
        List<CategoryDto.Response> response = categoryMapper.categoryToCategoryResponseList(categoryList);

        return new ResponseEntity(
                new MultiResponseDto<>(
                        response, pageCategories), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity getCategory(@PathVariable @Positive Long categoryId,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        Category category = categoryService.findVerifiedCategoryByCategoryId(categoryId);

        if(category.getCategoryType() == CategoryType.PROBLEM){
        Page<CategorizedProblem> categorizedProblemsPage = categorizedProblemService.findCategorizedProblemsByCategoryId(categoryId, page-1, size);

        CategoryDto.CategoryProblemPageResponse response = categoryMapper.categoryToCategoryProblemPageResponse(category, categorizedProblemsPage);

        return ResponseEntity.ok(response);
        }else{
            Page<CategorizedSummary> categorizedSummaryPage = categorizedSummaryService.findCategorziedSummarysByCategoryId(categoryId, page - 1, size);

            CategoryDto.CategorySummaryPageResponse response = categoryMapper.categoryToCategorySummaryPageResponse(category, categorizedSummaryPage);

            return ResponseEntity.ok(response);
        }

    }

}
