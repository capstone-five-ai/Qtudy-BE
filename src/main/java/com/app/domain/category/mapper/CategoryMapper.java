package com.app.domain.category.mapper;

import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.entity.Category;
import org.mapstruct.Mapper;

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

    default CategoryDto.CategoryProblemResponse categoryToCategoryProblemResponse(Category category){
        return CategoryDto.CategoryProblemResponse.of(category);
    }
}
