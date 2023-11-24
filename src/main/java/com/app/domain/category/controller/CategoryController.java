package com.app.domain.category.controller;

import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.entity.Category;
import com.app.domain.category.mapper.CategoryMapper;
import com.app.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/category")
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

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
}
