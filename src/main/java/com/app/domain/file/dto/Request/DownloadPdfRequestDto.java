package com.app.domain.file.dto.Request;


import com.app.global.config.ENUM.PdfType;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DownloadPdfRequestDto {
    PdfType pdfType;
}
