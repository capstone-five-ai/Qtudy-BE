package com.app.domain.summary.aigeneratedsummary.doc;

import com.app.domain.summary.aigeneratedsummary.dto.Summary.Response.SummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "AI 생성 요약", description = "AI 생성 요약 API")
public interface AiGeneratedSummaryApi {

  @Operation(summary = "요약 가져오기", description = "파일 ID로 요약을 가져옵니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약이 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = SummaryResponseDto.class,
                  example = "{ \"isWriter\": true, \"response\": { \"aiGeneratedSummaryId\": 1, \"summaryTitle\": \"샘플 요약\", \"summaryContent\": \"요약 내용\" } }"))),
      @ApiResponse(responseCode = "404", description = "요약을 찾을 수 없습니다.")
  })
  ResponseEntity<SummaryResponseDto> GetSummary(
      @Parameter(description = "파일 ID", required = true, example = "1")
      @PathVariable @Positive Long fileId,
      HttpServletRequest httpServletRequest
  );
}
