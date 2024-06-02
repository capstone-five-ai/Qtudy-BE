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
@Schema(name = "UpdateFileRequestDto", description = "파일 업데이트 요청 DTO")
public class UpdateFileRequestDto {
    @Schema(description = "새 파일 이름", example = "new_sample.pdf")
    private String newFileName;
}
