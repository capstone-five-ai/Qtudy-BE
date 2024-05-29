package com.app.domain.file.dto.Request;


import com.app.global.config.ENUM.PdfType;
import com.app.global.config.ENUM.ProblemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "DownloadPdfRequestDto", description = "PDF 다운로드 요청 DTO")
public class DownloadPdfRequestDto {
    @Schema(description = "PDF 타입", example = "PROBLEM")
    private PdfType pdfType;
}
