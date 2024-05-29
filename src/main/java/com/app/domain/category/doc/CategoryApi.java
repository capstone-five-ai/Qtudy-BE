package com.app.domain.category.doc;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.dto.CategoryDto;
import com.app.domain.common.MultiResponseDto;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Tag(name = "카테고리", description = "카테고리 API")
public interface CategoryApi {

  @Operation(summary = "새 카테고리 생성", description = "새 카테고리를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 생성되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryDto.Response.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
  })
  ResponseEntity<CategoryDto.Response> createCategory(
      @Valid @RequestBody CategoryDto.RequestDto categoryReqeustDto,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 수정되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
  })
  ResponseEntity<CategoryDto.Response> updateCategory(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId,
      @Valid @RequestBody CategoryDto.RequestDto categoryReqeustDto);

  @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 삭제되었습니다."),
      @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
  })
  ResponseEntity<Void> deleteCategory(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId);

  @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리 목록이 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = MultiResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
  })
  ResponseEntity<MultiResponseDto<CategoryDto.Response>> getCategories(
      @Parameter(description = "페이지 번호", required = true, example = "1")
      @Positive @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기", required = true, example = "10")
      @Positive @RequestParam(value = "size", defaultValue = "10") int size,
      @Parameter(description = "카테고리 유형", required = true)
      @RequestParam(value = "categoryType") CategoryType categoryType,
      HttpServletRequest httpServletRequest);

  @Operation(summary = "카테고리 조회", description = "ID로 카테고리를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 조회되었습니다.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(oneOf = {CategoryDto.CategoryProblemPageResponse.class, CategoryDto.CategorySummaryPageResponse.class}))),
      @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
  })
  ResponseEntity<?> getCategory(
      @Parameter(description = "카테고리 ID", required = true, example = "1")
      @PathVariable @Positive Long categoryId,
      @Parameter(description = "페이지 번호", required = true, example = "1")
      @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기", required = true, example = "10")
      @RequestParam(value = "size", defaultValue = "10") int size);
}
