package com.app.domain.category.dto;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.entity.Category;
import com.app.global.config.ENUM.GeneratedType;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryProblemResponse {
        private Long categoryId;

        private String categoryName;

        private List<CategorizedProblemResponse> categorizedProblemResponses;

        public static CategoryProblemResponse of(Category category){
            List<CategorizedProblemResponse> categorizedProblemResponses = category.getCategorizedProblems().stream()
                    .map(CategorizedProblemResponse::of)
                    .collect(Collectors.toList());

            return CategoryProblemResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categorizedProblemResponses(categorizedProblemResponses)
                    .build();
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategorizedProblemResponse{
        private Long categorizedProblemId;

        private GeneratedType problemGeneratedBy;

        private ProblemType problemType;

        private String problemName;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;

        public static CategorizedProblemResponse of(CategorizedProblem categorizedProblem) {
                        return CategorizedProblemResponse.builder()
                    .categorizedProblemId(categorizedProblem.getCategorizedProblemId())
                    .problemGeneratedBy(categorizedProblem.getMemberSavedProblem() != null ?
                            GeneratedType.MEMBER : GeneratedType.AI)
                    .problemType(categorizedProblem.getMemberSavedProblem() != null ?
                            categorizedProblem.getMemberSavedProblem().getProblemType() :
                            categorizedProblem.getAiGeneratedProblem().getProblemType())
                    .problemName(categorizedProblem.getMemberSavedProblem() != null ?
                            categorizedProblem.getMemberSavedProblem().getProblemName() :
                            categorizedProblem.getAiGeneratedProblem().getProblemName())
                    .createTime(categorizedProblem.getCreateTime())
                    .updateTime(categorizedProblem.getUpdateTime())
                    .build();
        }
    }


}
