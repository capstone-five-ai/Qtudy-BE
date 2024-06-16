package com.app.domain.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SummaryDto {

    @Getter
    @Schema(name = "SummaryPatchDto", description = "요약 수정 요청 DTO")
    @AllArgsConstructor
    public static class Patch {
        @Schema(description = "요약 제목", example = "새로운 요약 제목")
        private String summaryTitle;

        @Schema(description = "요약 내용", example = "새로운 요약 내용")
        private String summaryContent;
    }

    @Getter
    @AllArgsConstructor
    @Schema(name = "SummaryPdfResponseDto", description = "PDF 응답 DTO")
    public static class pdfResponse {
        @Schema(description = "PDF 내용", example = "PDF 파일의 바이트 배열")
        private byte[] pdfContent;

        @Schema(description = "PDF 제목", example = "PDF 제목")
        private String title;
    }
}
