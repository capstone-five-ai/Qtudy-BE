package com.app.domain.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SummaryDto {

    @Getter
    @AllArgsConstructor
    public static class pdfResponse{
        private byte[] pdfContent;
        private String title;
    }
}
