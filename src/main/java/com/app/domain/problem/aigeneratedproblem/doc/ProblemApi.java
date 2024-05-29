package com.app.domain.problem.aigeneratedproblem.doc;

import com.app.domain.problem.aigeneratedproblem.dto.Problem.Response.ProblemResponseDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "AI 생성 문제", description = "AI 생성 문제 API")
public interface ProblemApi {

  @Operation(summary = "파일의 전체 문제정보를 가져옴", description = "파일 ID로 전체 문제정보를 가져옵니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProblemResponseDto.class,
                  example = "{ \"isWriter\": true, \"problems\": [{ \"aiGeneratedProblemId\": 1, \"problemName\": \"문제 1\", \"problemChoices\": [\"A\", \"B\", \"C\", \"D\"], \"problemAnswer\": \"A\", \"problemCommentary\": \"해설\" }] }"))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<ProblemResponseDto> GetFileProblems(
      HttpServletRequest httpServletRequest,
      @Parameter(description = "파일 ID", required = true, example = "1")
      @PathVariable int fileId
  );
}
