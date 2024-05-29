package com.app.domain.summary.membersavedsummary.doc;

import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;

@Tag(name = "회원 저장 요약", description = "회원 저장 요약 API")
public interface MemberSavedSummaryApi {

  @Operation(summary = "새 요약 생성", description = "새 저장 요약을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedSummaryDto.Response.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<MemberSavedSummaryDto.Response> createSummary(
      @Valid @RequestBody MemberSavedSummaryDto.Post summaryPostDto,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "요약 PDF 다운로드", description = "ID로 요약 PDF를 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 다운로드되었습니다.",
          content = @Content(mediaType = "application/pdf")),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<byte[]> downloadSummaryPdf(
      @Parameter(description = "요약 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedSummaryId) throws IOException;

  @Operation(summary = "요약 수정", description = "ID로 요약을 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedSummaryDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<MemberSavedSummaryDto.Response> updateSummary(
      @Parameter(description = "요약 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedSummaryId,
      @Valid @RequestBody MemberSavedSummaryDto.Patch summaryPatchDto);

  @Operation(summary = "요약 삭제", description = "ID로 요약을 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<Void> deleteSummary(
      @Parameter(description = "요약 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedSummaryId);

  @Operation(summary = "요약 PDF 보기", description = "ID로 요약 PDF를 봅니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "PDF가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/pdf")),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ModelAndView viewPdf(
      @Parameter(description = "요약 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedSummaryId,
      Model model) throws IOException;

  @Operation(summary = "요약 조회", description = "ID로 요약을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedSummaryDto.LinkedSharedResponse.class))),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<MemberSavedSummaryDto.LinkedSharedResponse> getMemberSavedSummary(
      @Parameter(description = "요약 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedSummaryId,
      HttpServletRequest httpServletRequest);
}
