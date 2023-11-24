package com.app.domain.category.dto;

import com.app.domain.category.contsant.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class CategoryDto {

    @Getter
    public static class RequestDto{
        @NotBlank
        private String categoryName;

        private CategoryType categoryType;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long categoryId;

        private String categoryName;

        private CategoryType categoryType;
    }


}
