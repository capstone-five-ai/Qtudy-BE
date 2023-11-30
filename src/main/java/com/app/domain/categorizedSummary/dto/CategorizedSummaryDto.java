package com.app.domain.categorizedSummary.dto;

import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

        public static Response of(CategorizedSummary categorizedSummary) {
            return Response.builder()
                    .categorizedSummaryId(categorizedSummary.getCategorizedSummaryId())
                    .summaryTitle(categorizedSummary.getMemberSavedSummary() != null ?
                            categorizedSummary.getMemberSavedSummary().getSummaryTitle() :
                            categorizedSummary.getAiGeneratedSummary().getSummaryTitle())
                    .summaryContent(categorizedSummary.getMemberSavedSummary() != null ?
                            categorizedSummary.getMemberSavedSummary().getSummaryContent() :
                            categorizedSummary.getAiGeneratedSummary().getSummaryContent())
                    .build();
        }
    }
}
