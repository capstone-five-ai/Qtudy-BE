package com.app.domain.categorizedsummary.doc;

import com.app.domain.categorizedsummary.dto.CategorizedSummaryDto;
import com.app.domain.summary.dto.SummaryDto;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;

@Tag(name = "분류된 요약", description = "분류된 요약 API")
public interface CategorizedSummaryApi {

  @Operation(summary = "새 요약 생성", description = "새 분류된 요약을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedSummaryDto.PostResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<CategorizedSummaryDto.PostResponse> createCategorizedSummary(
      @Valid @RequestBody CategorizedSummaryDto.Post categorizedSummaryPostDto);

  @Operation(summary = "요약 PDF 다운로드", description = "분류된 요약 PDF를 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/pdf")),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<byte[]> downloadSummaryPdf(
      @Parameter(description = "분류된 요약 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedSummaryId) throws IOException;

  @Operation(summary = "요약 수정", description = "분류된 요약을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedSummaryDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<CategorizedSummaryDto.Response> updateCategorizedSummary(
      @Parameter(description = "분류된 요약 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedSummaryId,
      @Valid @RequestBody SummaryDto.Patch problemPatchDto);

  @Operation(summary = "요약 삭제", description = "분류된 요약을 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<Void> deleteCategorizedSummary(
      @Parameter(description = "분류된 요약 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedSummaryId);

  @Operation(summary = "요약 조회", description = "ID로 분류된 요약을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedSummaryDto.LinkedSharedResponse.class))),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<CategorizedSummaryDto.LinkedSharedResponse> getCategorizedSummary(
      @Parameter(description = "분류된 요약 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedSummaryId,
      HttpServletRequest httpServletRequest);
}
