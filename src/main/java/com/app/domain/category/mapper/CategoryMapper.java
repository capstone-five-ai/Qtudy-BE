package com.app.domain.category.mapper;

import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category categoryRequestDtoToCategory(CategoryDto.RequestDto categoryPostDto);

    CategoryDto.Response categoryToCategoryResponse(Category category);
}
