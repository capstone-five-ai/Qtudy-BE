package com.app.domain.category.mapper;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryRequestDtoToCategory(CategoryDto.RequestDto categoryPostDto);

    CategoryDto.Response categoryToCategoryResponse(Category category);

    default List<CategoryDto.Response> categoryToCategoryResponseList(List<Category> categoryList) {
        List<CategoryDto.Response> responseList = new ArrayList<>(categoryList.size());

        for (Category category : categoryList) {
            responseList.add(categoryToCategoryResponse(category));
        }
        return responseList;
    }

    default CategoryDto.CategoryProblemPageResponse categoryToCategoryProblemPageResponse(Category category, Page<CategorizedProblem> categorizedProblemsPage){
        return CategoryDto.CategoryProblemPageResponse.of(category, categorizedProblemsPage);
    }

    default CategoryDto.CategorySummaryPageResponse categoryToCategorySummaryPageResponse(Category category, Page<CategorizedSummary> categorizedSummaryPage) {
        return CategoryDto.CategorySummaryPageResponse.of(category, categorizedSummaryPage);
    }

    default CategoryDto.CategoryProblemResponse categoryToCategoryProblemResponse(Category category){
        return CategoryDto.CategoryProblemResponse.of(category);
    }
}
