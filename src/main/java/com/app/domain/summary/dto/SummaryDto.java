package com.app.domain.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SummaryDto {

    @Getter
    public static class Patch{
        private String summaryTitle;

        private String summaryContent;
    }

    @Getter
    @AllArgsConstructor
    public static class pdfResponse{
        private byte[] pdfContent;
        private String title;
    }
}
