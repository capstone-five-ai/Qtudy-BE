package com.app.domain.file.dto.Request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "DuplicateFileNameRequestDto", description = "파일 이름 중복 체크 요청 DTO")
public class DuplicateFileNameRequestDto {
    @Schema(description = "파일 이름", example = "sample.pdf")
    private String fileName;
    @Schema(description = "파일 타입", example = "PDF")
    private String type;
}
