package com.app.domain.file.dto.Response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "DownloadPdfResponseDto", description = "PDF 다운로드 응답 DTO")
public class DownloadPdfResponseDto {
    @Schema(description = "파일 URL", example = "http://example.com/sample.pdf")
    private String fileUrl;
}
