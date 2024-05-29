package com.app.domain.file.dto.Response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(name = "DuplicateFileNameResponseDto", description = "파일 이름 중복 체크 응답 DTO")
public class DuplicateFileNameResponseDto {
    @Schema(description = "중복 여부", example = "true")
    private Boolean duplicate;
}
