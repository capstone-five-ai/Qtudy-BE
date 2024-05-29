package com.app.domain.category.dto;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.entity.Category;
import com.app.domain.common.MultiResponseDto;
import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import com.app.global.config.ENUM.GeneratedType;
import com.app.global.config.ENUM.ProblemType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "CategoryRequestDto", description = "카테고리 요청 DTO")
    public static class RequestDto {
        @NotBlank
        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "카테고리 유형", example = "PROBLEM")
        private CategoryType categoryType;
    }

    @Getter
    @AllArgsConstructor
    @Schema(name = "CategoryResponse", description = "카테고리 응답 DTO")
    public static class Response {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "카테고리 유형", example = "PROBLEM")
        private CategoryType categoryType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CategoryProblemPageResponse", description = "카테고리 문제 페이지 응답 DTO")
    public static class CategoryProblemPageResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "분류된 문제 응답 목록", implementation = MultiResponseDto.class)
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
    @Schema(name = "CategoryProblemResponse", description = "카테고리 문제 응답 DTO")
    public static class CategoryProblemResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "분류된 문제 응답 목록", implementation = CategorizedProblemResponse.class)
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
    @Schema(name = "CategorizedProblemResponse", description = "분류된 문제 응답 DTO")
    public static class CategorizedProblemResponse {
        @Schema(description = "분류된 문제 ID", example = "1")
        private Long categorizedProblemId;

        @Schema(description = "문제 생성 유형", example = "MEMBER")
        private GeneratedType problemGeneratedBy;

        @Schema(description = "문제 유형", example = "MULTIPLE")
        private ProblemType problemType;

        @Schema(description = "문제 이름", example = "샘플 문제")
        private String problemName;

        @Schema(description = "생성 시간", example = "2023-05-29T12:34:56")
        private LocalDateTime createTime;

        @Schema(description = "수정 시간", example = "2023-05-29T12:34:56")
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
    @Schema(name = "CategorySummaryPageResponse", description = "카테고리 요약 페이지 응답 DTO")
    public static class CategorySummaryPageResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "분류된 요약 응답 목록", implementation = MultiResponseDto.class)
        private MultiResponseDto<CategorizedSummaryResponse> categorizedSummaryResponses;

        public static CategorySummaryPageResponse of(Category category,
            Page<CategorizedSummary> categorizedSummarysPage){
            List<CategorizedSummaryResponse> categorizedSummaryResponses = categorizedSummarysPage.getContent().stream()
                .map(CategorizedSummaryResponse::of)
                .collect(Collectors.toList());

            MultiResponseDto<CategorizedSummaryResponse> multiResponseDto = new MultiResponseDto<>(categorizedSummaryResponses, categorizedSummarysPage);

            return CategorySummaryPageResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categorizedSummaryResponses(multiResponseDto)
                .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CategorySummaryResponse", description = "카테고리 요약 응답 DTO")
    public static class CategorySummaryResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "분류된 요약 응답 목록", implementation = CategorizedSummaryResponse.class)
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
    @Schema(name = "CategorizedSummaryResponse", description = "분류된 요약 응답 DTO")
    public static class CategorizedSummaryResponse {
        @Schema(description = "분류된 요약 ID", example = "1")
        private Long categorizedSummaryId;

        @Schema(description = "요약 생성 유형", example = "MEMBER")
        private GeneratedType summaryGeneratedBy;

        @Schema(description = "요약 제목", example = "샘플 요약 제목")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "샘플 요약 내용")
        private String summaryContent;

        @Schema(description = "생성 시간", example = "2023-05-29T12:34:56")
        private LocalDateTime createTime;

        @Schema(description = "수정 시간", example = "2023-05-29T12:34:56")
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
