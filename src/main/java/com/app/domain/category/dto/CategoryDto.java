package com.app.domain.category.dto;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.entity.Category;
import com.app.domain.common.MultiResponseDto;
import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import com.app.global.config.ENUM.GeneratedType;
import com.app.global.config.ENUM.ProblemType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
    public static class CategoryProblemPageResponse {
        private Long categoryId;

        private String categoryName;

//        private List<CategorizedProblemResponse> categorizedProblemResponses;
        private MultiResponseDto<CategorizedProblemResponse> categorizedProblemResponses;
        public static CategoryProblemPageResponse of(Category category,
                                                     Page<CategorizedProblem> categorizedProblemsPage){
            List<CategorizedProblemResponse> categorizedProblemResponses = categorizedProblemsPage.getContent().stream()
                    .map(CategorizedProblemResponse::of)
                    .collect(Collectors.toList());

            MultiResponseDto<CategorizedProblemResponse> multiResponseDto = new MultiResponseDto<>(categorizedProblemResponses, categorizedProblemsPage);

            return CategoryProblemPageResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categorizedProblemResponses(multiResponseDto)
                    .build();
        }
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
            List<CategorizedProblemResponse> categorizedProblemResponsesList = category.getCategorizedProblems().stream()
                    .map(CategorizedProblemResponse::of)
                    .collect(Collectors.toList());

            return CategoryProblemResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categorizedProblemResponses(categorizedProblemResponsesList)
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
                    .problemGeneratedBy(categorizedProblem.getProblem().isMemberSavedProblem() ?
                            GeneratedType.MEMBER : GeneratedType.AI)
                    .problemType(categorizedProblem.getProblem().getProblemType())
                    .problemName(categorizedProblem.getProblem().getProblemName())
                    .createTime(categorizedProblem.getCreateTime())
                    .updateTime(categorizedProblem.getUpdateTime())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummaryPageResponse {
        private Long categoryId;

        private String categoryName;
        private MultiResponseDto<CategorizedSummaryResponse> categorizedProblemResponses;
        public static CategorySummaryPageResponse of(Category category,
                                                     Page<CategorizedSummary> categorizedSummarysPage){
            List<CategorizedSummaryResponse> categorizedSummaryResponses = categorizedSummarysPage.getContent().stream()
                    .map(CategorizedSummaryResponse::of)
                    .collect(Collectors.toList());

            MultiResponseDto<CategorizedSummaryResponse> multiResponseDto = new MultiResponseDto<>(categorizedSummaryResponses, categorizedSummarysPage);

            return CategorySummaryPageResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categorizedProblemResponses(multiResponseDto)
                    .build();
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummaryResponse {
        private Long categoryId;

        private String categoryName;

        private List<CategorizedSummaryResponse> categorizedSummaryResponses;
        public static CategorySummaryResponse of(Category category){
            List<CategorizedSummaryResponse> categorizedSummaryResponsesList = category.getCategorizedSummaries().stream()
                    .map(CategorizedSummaryResponse::of)
                    .collect(Collectors.toList());

            return CategorySummaryResponse.builder()
                    .categoryId(category.getCategoryId())
                    .categoryName(category.getCategoryName())
                    .categorizedSummaryResponses(categorizedSummaryResponsesList)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorizedSummaryResponse {
        private Long categorizedSummaryId;

        private GeneratedType summaryGeneratedBy;

        private String summaryTitle;

        private String summaryContent;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;

        public static CategorizedSummaryResponse of(CategorizedSummary categorizedSummary) {
            return CategorizedSummaryResponse.builder()
                    .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                    .summaryGeneratedBy(categorizedSummary.getSummary() instanceof MemberSavedSummary ?
                            GeneratedType.MEMBER : GeneratedType.AI)
                    .summaryTitle(categorizedSummary.getSummary().getSummaryTitle())
                    .summaryContent(categorizedSummary.getSummary().getSummaryContent())
                    .createTime(categorizedSummary.getCreateTime())
                    .updateTime(categorizedSummary.getUpdateTime())
                    .build();
        }
    }
}
