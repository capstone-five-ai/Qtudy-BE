package com.app.domain.file.doc;

import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.DuplicateFileNameRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.DownloadPdfResponseDto;
import com.app.domain.file.dto.Response.DuplicateFileNameResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name = "파일", description = "파일 API")
public interface FileApi {

  @Operation(summary = "파일 이름 수정", description = "사용자가 생성한 파일 이름을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 이름이 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<String> updateFile(
      @Parameter(description = "파일 ID", required = true, example = "1")
      @PathVariable Long fileId,
      @Valid @RequestBody UpdateFileRequestDto updateFileRequestDto);

  @Operation(summary = "문제 PDF 다운로드", description = "문제 PDF를 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 다운로드되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = DownloadPdfResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없습니다.")
  })
  ResponseEntity<DownloadPdfResponseDto> downloadProblemPdf(
      @Parameter(description = "파일 ID", required = true, example = "1")
      @PathVariable Long fileId,
      @RequestBody DownloadPdfRequestDto downloadPdfRequestDto);

  @Operation(summary = "파일 삭제", description = "문제 파일을 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일이 성공적으로 삭제되었습니다.",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없습니다.")
  })
  ResponseEntity<String> deleteProblemFile(
      @Parameter(description = "파일 ID", required = true, example = "1")
      @PathVariable Long fileId);

  @Operation(summary = "파일 이름 중복 체크", description = "파일 이름의 중복 여부를 확인합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 이름 중복 여부가 성공적으로 확인되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = DuplicateFileNameResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<DuplicateFileNameResponseDto> duplicateFileName(
      @RequestBody DuplicateFileNameRequestDto duplicateFileNameRequestDto);
}
