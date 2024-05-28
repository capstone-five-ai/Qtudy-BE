package com.app.domain.categorizedsummary.dto;

import com.app.domain.categorizedsummary.entity.CategorizedSummary;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class Post{
        private List<Long> categoryIdList;

        private Integer summaryId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostResponse{
        private List<Long> categorizedSummaryId;

        private List<Long> categoryId;

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
    public static class Response{
        private Long categorizedSummaryId;

        private String summaryTitle;

        private String summaryContent;

        private String categoryName;

        private Long categoryId;

        private CategorizedSummaryResponse previousSummary;

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
    public static class LinkedSharedResponse{
        private Response response;

        @JsonProperty("isWriter")
        private Boolean isWriter;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategorizedSummaryResponse{
        private Long categorizedSummaryId;

        private String categorizedSummaryName;

        public static CategorizedSummaryResponse of(CategorizedSummary categorizedSummary) {
            return CategorizedSummaryResponse.builder()
                    .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                    .categorizedSummaryName(categorizedSummary.getSummary().getSummaryTitle())
                    .build();
        }
    }
}
