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
@Schema(name = "DeleteProblemFileDto", description = "문제 파일 삭제 요청 DTO")
public class DeleteProblemFileDto {
    @Schema(description = "파일 ID", example = "1")
    private int fileId;
}