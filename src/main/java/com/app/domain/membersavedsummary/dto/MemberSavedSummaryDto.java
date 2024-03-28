package com.app.domain.membersavedsummary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static class LinkedSharedResponse{
        private Response response;

        @JsonProperty("isWriter")
        private boolean isWriter;

        public LinkedSharedResponse(Response response, boolean isWriter) {
            this.response = response;
            this.isWriter = isWriter;
        }

        public Response getResponse() {
            return response;
        }

        public boolean getisWriter() {
            return isWriter;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class pdfResponse{
        private byte[] pdfContent;
        private String title;
    }
}
