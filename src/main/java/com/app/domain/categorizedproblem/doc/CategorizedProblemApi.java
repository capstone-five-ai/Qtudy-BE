package com.app.domain.categorizedproblem.doc;

import com.app.domain.categorizedproblem.dto.CategorizedProblemDto;
import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "카테고리화 문제", description = "카테고리화 문제 API")
public interface CategorizedProblemApi {

  @Operation(summary = "새 문제 생성", description = "새 카테고리화 문제를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedProblemDto.PostResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<CategorizedProblemDto.PostResponse> createCategorizedProblem(
      @Valid @RequestBody CategorizedProblemDto.Post categorizedProblemPostDto);

  @Operation(summary = "문제 PDF 다운로드", description = "카테고리별로 문제 PDF를 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/pdf")),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<byte[]> downloadCategorizedProblemsPdf(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId) throws IOException;

  @Operation(summary = "문제 정답 PDF 다운로드", description = "카테고리별로 문제 정답 PDF를 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/pdf")),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<byte[]> downloadCategorizedProblemsAnswerPdf(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId) throws IOException;

  @Operation(summary = "문제 수정", description = "카테고리화 문제를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedProblemDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<CategorizedProblemDto.Response> updateCategorizedProblem(
      @Parameter(description = "카테고리화 문제 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedProblemId,
      @Valid @RequestBody MemberSavedProblemDto.Patch problemPatchDto);

  @Operation(summary = "문제 삭제", description = "카테고리화 문제를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<Void> deleteCategorizedProblem(
      @Parameter(description = "카테고리화 문제 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedProblemId);

  @Operation(summary = "문제 조회", description = "ID로 카테고리화 문제를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategorizedProblemDto.LinkedSharedResponse.class))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<CategorizedProblemDto.LinkedSharedResponse> getCategorizedProblem(
      @Parameter(description = "카테고리화 문제 ID", required = true, example = "1")
      @PathVariable @Positive Long categorizedProblemId,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "PDF 보기", description = "카테고리별로 문제 PDF를 봅니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 생성되었습니다."),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ModelAndView viewPdf(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId, Model model) throws IOException;
}
