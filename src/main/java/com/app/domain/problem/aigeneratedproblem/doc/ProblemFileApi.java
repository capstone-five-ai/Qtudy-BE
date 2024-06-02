package com.app.domain.problem.aigeneratedproblem.doc;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Response.AiGeneratedProblemResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

@Tag(name = "문제 파일", description = "문제 파일 API")
public interface ProblemFileApi {

  @Operation(summary = "텍스트기반 문제파일 생성", description = "텍스트를 기반으로 문제파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGeneratedProblemResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
  })
  ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemFileByText(
      HttpServletRequest httpServletRequest,
      @Valid @RequestBody AiGenerateProblemDto aiGenerateProblemDto
  );

  @Operation(summary = "이미지기반 문제파일 생성", description = "이미지 파일을 기반으로 문제파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGeneratedProblemResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
  })
  ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemImageByImage(
      HttpServletRequest httpServletRequest,
      @RequestParam("file") List<MultipartFile> file,
      @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto
  );

  @Operation(summary = "PDF기반 문제파일 생성", description = "PDF 파일을 기반으로 문제파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGeneratedProblemResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
  })
  ResponseEntity<AiGeneratedProblemResponseDto> AiGenerateProblemFileByPdf(
      HttpServletRequest httpServletRequest,
      @RequestParam("file") MultipartFile pdfFile,
      @ModelAttribute AiGenerateProblemDto aiGenerateProblemDto
  );

  @Operation(summary = "모든 문제 리스트 가져오기", description = "사용자가 생성한 모든 문제 리스트를 페이지 번호로 가져옵니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "문제가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "404", description = "문제를 찾을 수 없습니다.")
  })
  ResponseEntity<Page<FileListResponseDto>> allAiProblemFileList(
      HttpServletRequest httpServletRequest,
      @Parameter(description = "페이지 번호", required = true, example = "1")
      @PathVariable int pageNumber
  );
}
