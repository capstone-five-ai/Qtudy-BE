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

        private Long memberSavedSummaryId;

        private Integer aiGeneratedSummaryId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostResponse{
        private List<Long> categorizedSummaryId;

        private List<Long> categoryId;

        private Long memberSavedSummaryId;

        private Integer aiGeneratedSummaryId;

        public static PostResponse of(List<Long> categorizedSummaryIdList, CategorizedSummaryDto.Post categorizedSummaryPostDto){
            return PostResponse.builder()
                    .categorizedSummaryId(categorizedSummaryIdList)
                    .categoryId(categorizedSummaryPostDto.getCategoryIdList())
                    .memberSavedSummaryId(categorizedSummaryPostDto.getMemberSavedSummaryId())
                    .aiGeneratedSummaryId(categorizedSummaryPostDto.getAiGeneratedSummaryId())
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

        private List<CategorizedSummaryResponse> categorizedSummaryResponseList;

        public static Response of(CategorizedSummary categorizedSummary) {

            List<CategorizedSummary> summaries = categorizedSummary.getCategory().getCategorizedSummaries();

            CategorizedSummary previousSummary = summaries.stream()
                    .filter(cs -> cs.getCategorizedSummaryId() < categorizedSummary.getCategorizedSummaryId())
                    .max(Comparator.comparing(CategorizedSummary::getCategorizedSummaryId))
                    .orElse(null);

            CategorizedSummary nextSummary = summaries.stream()
                    .filter(cs -> cs.getCategorizedSummaryId() > categorizedSummary.getCategorizedSummaryId())
                    .min(Comparator.comparing(CategorizedSummary::getCategorizedSummaryId))
                    .orElse(null);

            List<CategorizedSummaryResponse> categorizedSummaryResponses = Stream.of(previousSummary, nextSummary)
                    .map(CategorizedSummaryResponse::of)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return Response.builder()
                    .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                    .summaryTitle(categorizedSummary.getMemberSavedSummary() != null ?
                            categorizedSummary.getMemberSavedSummary().getSummaryTitle() :
                            categorizedSummary.getAiGeneratedSummary().getSummaryTitle())
                    .summaryContent(categorizedSummary.getMemberSavedSummary() != null ?
                            categorizedSummary.getMemberSavedSummary().getSummaryContent() :
                            categorizedSummary.getAiGeneratedSummary().getSummaryContent())
                    .categoryName(categorizedSummary.getCategory().getCategoryName())
                    .categoryId(categorizedSummary.getCategory().getCategoryId())
                    .categorizedSummaryResponseList(categorizedSummaryResponses)
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
                    .categorizedSummaryName(categorizedSummary.getMemberSavedSummary() != null ?
                            categorizedSummary.getMemberSavedSummary().getSummaryTitle() :
                            categorizedSummary.getAiGeneratedSummary().getSummaryTitle())
                    .build();
        }
    }
}
