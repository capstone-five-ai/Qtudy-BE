package com.app.domain.problem.membersavedproblem.doc;

import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 저장 문제", description = "회원 저장 문제 API")
public interface MemberSavedProblemApi {

  @Operation(summary = "새 문제 생성", description = "회원의 새 저장 문제를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedProblemDto.Response.class,
                  example = "{ \"problemId\": 1, \"problemName\": \"샘플 문제\", \"problemAnswer\": \"정답 예시\", \"problemCommentary\": \"이것은 샘플 해설입니다.\", \"problemType\": \"다중 선택\", \"problemChoices\": [\"옵션 A\", \"옵션 B\"] }"))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<MemberSavedProblemDto.Response> createProblem(
      @Valid @RequestBody MemberSavedProblemDto.Post problemPostDto,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "문제 수정", description = "회원의 기존 저장 문제를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedProblemDto.Response.class,
                  example = "{ \"problemId\": 1, \"problemName\": \"수정된 문제 이름\", \"problemAnswer\": \"수정된 정답\", \"problemCommentary\": \"수정된 해설\", \"problemType\": \"다중 선택\", \"problemChoices\": [\"수정된 옵션 A\", \"수정된 옵션 B\"] }"))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<MemberSavedProblemDto.Response> updateProblem(
      @Parameter(description = "문제 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedProblemId,
      @Valid @RequestBody MemberSavedProblemDto.Patch problemPatchDto);

  @Operation(summary = "문제 삭제", description = "회원의 저장 문제를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<Void> deleteProblem(
      @Parameter(description = "문제 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedProblemId);

  @Operation(summary = "문제 조회", description = "ID로 회원의 저장 문제를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MemberSavedProblemDto.LinkSharedResponse.class,
                  example = "{ \"response\": { \"problemId\": 1, \"problemName\": \"샘플 문제\", \"problemAnswer\": \"정답 예시\", \"problemCommentary\": \"이것은 샘플 해설입니다.\", \"problemType\": \"다중 선택\", \"problemChoices\": [\"옵션 A\", \"옵션 B\"] }, \"isWriter\": true }"))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<MemberSavedProblemDto.LinkSharedResponse> getMemberSavedProblem(
      @Parameter(description = "문제 ID", required = true, example = "1")
      @PathVariable @Positive Long memberSavedProblemId,
      HttpServletRequest httpServletRequest);
}
