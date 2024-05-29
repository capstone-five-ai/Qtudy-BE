package com.app.domain.file.dto.Response;


import com.app.global.config.ENUM.DType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "FileListResponseDto", description = "파일 목록 응답 DTO")
public class FileListResponseDto {
    @Schema(description = "파일 ID", example = "1")
    private int fileId;

    @Schema(description = "파일 이름", example = "sample.pdf")
    private String fileName;

    @Schema(description = "파일 타입", example = "PROBLEM")
    private DType dtype;

    @Schema(description = "생성 시간", example = "2023-05-29T12:34:56")
    private LocalDateTime createTime;

    @Schema(description = "수정 시간", example = "2023-05-29T12:34:56")
    private LocalDateTime updateTime;
}
