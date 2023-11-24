package com.app.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class CategoryDto {

    @Getter
    public static class RequestDto{
        @NotBlank
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long categoryId;

        private String categoryName;
    }


}
