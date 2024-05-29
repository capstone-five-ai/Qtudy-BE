package com.app.domain.summary.membersavedsummary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class MemberSavedSummaryDto {

    @Getter
    @Schema(name = "MemberSavedSummaryPostDto", description = "새 요약 생성 요청 DTO")
    public static class Post {
        @NotBlank(message = "제목을 입력해주세요")
        @Schema(description = "요약 제목", example = "요약 제목")
        private String summaryTitle;

        @NotBlank(message = "내용을 입력해주세요")
        @Schema(description = "요약 내용", example = "요약 내용")
        private String summaryContent;
    }

    @Getter
    @Schema(name = "MemberSavedSummaryPatchDto", description = "요약 수정 요청 DTO")
    public static class Patch {
        @Schema(description = "요약 제목", example = "새로운 요약 제목")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "새로운 요약 내용")
        private String summaryContent;
    }

    @Getter
    @AllArgsConstructor
    @Schema(name = "MemberSavedSummaryResponseDto", description = "요약 응답 DTO")
    public static class Response {
        @Schema(description = "요약 ID", example = "1")
        private Long summaryId;

        @Schema(description = "요약 제목", example = "요약 제목")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "요약 내용")
        private String summaryContent;
    }

    @Schema(name = "MemberSavedSummaryLinkedSharedResponseDto", description = "공유된 요약 응답 DTO")
    public static class LinkedSharedResponse {
        @Schema(description = "요약 응답", implementation = Response.class)
        private Response response;

        @JsonProperty("isWriter")
        @Schema(description = "작성자인지 여부", example = "true")
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
    @Schema(name = "MemberSavedSummaryPdfResponseDto", description = "PDF 응답 DTO")
    public static class pdfResponse {
        @Schema(description = "PDF 내용", example = "PDF 파일의 바이트 배열")
        private byte[] pdfContent;

        @Schema(description = "PDF 제목", example = "PDF 제목")
        private String title;
    }
}