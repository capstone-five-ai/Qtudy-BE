package com.app.domain.memberSavedSummary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class MemberSavedSummaryDto {

    @Getter
    public static class Post{
        @NotBlank(message = "제목을 입력해주세요")
        private String summaryTitle;

        @NotBlank(message = "내용을 입력해주세요")
        private String summaryContent;
    }

    @Getter
    public static class Patch{
        private String summaryTitle;

        private String summaryContent;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private Long memberSavedSummaryId;

        private String summaryTitle;
        private String summaryContent;
    }
}
