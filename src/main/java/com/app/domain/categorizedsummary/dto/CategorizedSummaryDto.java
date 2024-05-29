package com.app.domain.categorizedsummary.dto;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategorizedSummaryDto {

    @Getter
    @Schema(name = "CategorizedSummaryPost", description = "새 카테고리화 요약 생성 요청 DTO")
    public static class Post {
        @Schema(description = "카테고리 ID 목록", example = "[1, 2]")
        private List<Long> categoryIdList;

        @Schema(description = "요약 ID", example = "1")
        private Integer summaryId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "CategorizedSummaryPostResponse", description = "새 카테고리화 요약 생성 응답 DTO")
    public static class PostResponse {
        @Schema(description = "카테고리화 요약 ID 목록", example = "[1, 2]")
        private List<Long> categorizedSummaryId;

        @Schema(description = "카테고리 ID 목록", example = "[1, 2]")
        private List<Long> categoryId;

        @Schema(description = "요약 ID", example = "1")
        private Integer summaryId;

        public static PostResponse of(List<Long> categorizedSummaryIdList, CategorizedSummaryDto.Post categorizedSummaryPostDto){
            return PostResponse.builder()
                .categorizedSummaryId(categorizedSummaryIdList)
                .categoryId(categorizedSummaryPostDto.getCategoryIdList())
                .summaryId(categorizedSummaryPostDto.getSummaryId())
                .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CategorizedSummaryResponse", description = "카테고리화 요약 응답 DTO")
    public static class Response {
        @Schema(description = "카테고리화 요약 ID", example = "1")
        private Long categorizedSummaryId;

        @Schema(description = "요약 제목", example = "샘플 요약 제목")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "샘플 요약 내용")
        private String summaryContent;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "이전 요약", implementation = CategorizedSummaryResponse.class)
        private CategorizedSummaryResponse previousSummary;

        @Schema(description = "다음 요약", implementation = CategorizedSummaryResponse.class)
        private CategorizedSummaryResponse nextSummary;

        public static Response of(CategorizedSummary categorizedSummary) {

            List<CategorizedSummary> summaries = categorizedSummary.getCategory().getCategorizedSummaries();

            CategorizedSummaryResponse previousSummaryResponse = summaries.stream()
                .filter(cp -> cp.getCategorizedSummaryId() < categorizedSummary.getCategorizedSummaryId())
                .max(Comparator.comparing(CategorizedSummary::getCategorizedSummaryId))
                .map(CategorizedSummaryResponse ::of)
                .orElse(null);

            CategorizedSummaryResponse nextSummaryResponse = summaries.stream()
                .filter(cp -> cp.getCategorizedSummaryId() > categorizedSummary.getCategorizedSummaryId())
                .min(Comparator.comparing(CategorizedSummary::getCategorizedSummaryId))
                .map(CategorizedSummaryResponse ::of)
                .orElse(null);

            return Response.builder()
                .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                .summaryTitle(categorizedSummary.getSummary().getSummaryTitle())
                .summaryContent(categorizedSummary.getSummary().getSummaryContent())
                .categoryName(categorizedSummary.getCategory().getCategoryName())
                .categoryId(categorizedSummary.getCategory().getCategoryId())
                .previousSummary(previousSummaryResponse)
                .nextSummary(nextSummaryResponse)
                .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Schema(name = "CategorizedSummaryLinkedSharedResponse", description = "공유된 카테고리화 요약 응답 DTO")
    public static class LinkedSharedResponse {
        @Schema(description = "요약 응답", implementation = Response.class)
        private Response response;

        @JsonProperty("isWriter")
        @Schema(description = "작성자인지 여부", example = "true")
        private Boolean isWriter;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name = "CategorizedSummarySimpleResponse", description = "카테고리화 요약 기본 응답 DTO")
    public static class CategorizedSummaryResponse {
        @Schema(description = "카테고리화 요약 ID", example = "1")
        private Long categorizedSummaryId;

        @Schema(description = "카테고리화 요약 이름", example = "샘플 요약 이름")
        private String categorizedSummaryName;

        public static CategorizedSummaryResponse of(CategorizedSummary categorizedSummary) {
            return CategorizedSummaryResponse.builder()
                .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                .categorizedSummaryName(categorizedSummary.getSummary().getSummaryTitle())
                .build();
        }
    }
}
