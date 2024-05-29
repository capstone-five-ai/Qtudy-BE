package com.app.domain.summary.aigeneratedsummary.doc;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request.AiGenerateSummaryDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Response.AiGenerateSummaryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "요약 파일 생성", description = "요약 파일 생성 API")
public interface SummaryFileApi {

  @Operation(summary = "텍스트 기반 요약 파일 생성", description = "텍스트를 기반으로 요약 파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGenerateSummaryResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryFileByText(
      HttpServletRequest httpServletRequest,
      @Valid @RequestBody AiGenerateSummaryDto aiGenerateSummaryDto);

  @Operation(summary = "이미지 기반 요약 파일 생성", description = "이미지를 기반으로 요약 파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGenerateSummaryResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryImageByImage(
      HttpServletRequest httpServletRequest,
      @Parameter(description = "이미지 파일 리스트", required = true)
      @RequestParam("file") List<MultipartFile> imageFile,
      @Parameter(description = "요약 파일 생성 요청 DTO", required = true)
      @ModelAttribute AiGenerateSummaryDto aiGenerateSummaryDto);

  @Operation(summary = "PDF 기반 요약 파일 생성", description = "PDF 파일을 기반으로 요약 파일을 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약 파일이 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AiGenerateSummaryResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<AiGenerateSummaryResponseDto> AiGenerateSummaryFileByPdf(
      HttpServletRequest httpServletRequest,
      @Parameter(description = "PDF 파일", required = true)
      @RequestParam("file") MultipartFile pdfFile,
      @Parameter(description = "요약 파일 생성 요청 DTO", required = true)
      @ModelAttribute AiGenerateSummaryDto aiGenerateSummaryDto);

  @Operation(summary = "모든 요약 파일 리스트 조회", description = "사용자가 생성한 모든 요약 파일 리스트를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "요약 파일 리스트가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = FileListResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "요약 파일 리스트를 찾을 수 없습니다.")
  })
  ResponseEntity<Page<FileListResponseDto>> allAiSummaryFileList(
      HttpServletRequest httpServletRequest,
      @Parameter(description = "페이지 번호", required = true, example = "1")
      @PathVariable int pageNumber);
}
