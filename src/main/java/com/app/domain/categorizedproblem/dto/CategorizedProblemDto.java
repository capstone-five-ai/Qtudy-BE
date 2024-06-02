package com.app.domain.categorizedproblem.dto;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.problem.entity.Problem;
import com.app.global.config.ENUM.ProblemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategorizedProblemDto {

    @Getter
    @Schema(name = "CategorizedProblemPost", description = "새 카테고리화 문제 생성 요청 DTO")
    public static class Post {

        @Schema(description = "카테고리 ID 목록", example = "[1, 2]")
        private List<Long> categoryIdList;

        @Schema(description = "문제 ID", example = "1")
        private Long problemId;
    }

    @Getter
    @Schema(name = "CategorizedProblemPatch", description = "카테고리화 문제 수정 요청 DTO")
    public static class Patch{
        @Schema(description = "문제 이름", example = "수정된 문제 이름")
        private String problemName;

        @Schema(description = "문제 정답", example = "수정된 정답")
        private String problemAnswer;

        @Schema(description = "문제 해설", example = "수정된 해설")
        private String problemCommentarty;

        @Schema(description = "문제 선택지", example = "[\"수정된 옵션 A\", \"수정된 옵션 B\"]")
        private List<String> problemChoices;
    }

    @Getter
    @AllArgsConstructor
    @Schema(name = "CategorizedProblemPostResponse", description = "새 카테고리화 문제 생성 응답 DTO")
    public static class PostResponse {
        @Schema(description = "카테고리화 문제 ID 목록", example = "[1, 2]")
        private List<Long> categorizedProblemId;

        @Schema(description = "카테고리 ID 목록", example = "[1, 2]")
        private List<Long> categoryId;

        @Schema(description = "문제 ID", example = "1")
        private Long problemId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CategorizedProblemResponse", description = "카테고리화 문제 응답 DTO")
    public static class Response {

        @Schema(description = "문제 ID", example = "1")
        private Long problemId;

        @Schema(description = "카테고리화 문제 ID", example = "1")
        private Long categorizedProblemId;

        @Schema(description = "문제 이름", example = "샘플 문제")
        private String problemName;

        @Schema(description = "문제 정답", example = "정답 예시")
        private String problemAnswer;

        @Schema(description = "문제 해설", example = "이것은 샘플 해설입니다.")
        private String problemCommentary;

        @Schema(description = "문제 유형", example = "다중 선택")
        private ProblemType problemType;

        @Schema(description = "문제 선택지 목록", example = "[\"옵션 A\", \"옵션 B\"]")
        private List<String> problemChoices;

        @Schema(description = "카테고리 이름", example = "샘플 카테고리")
        private String categoryName;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "이전 문제", implementation = CategorizedProblemResponse.class)
        private CategorizedProblemResponse previousProblem;

        @Schema(description = "다음 문제", implementation = CategorizedProblemResponse.class)
        private CategorizedProblemResponse nextProblem;

        public static Response of(CategorizedProblem categorizedProblem) {

            List<CategorizedProblem> problems = categorizedProblem.getCategory().getCategorizedProblems();
            CategorizedProblemResponse previousProblemResponse = problems.stream()
                .filter(cp -> cp.getCategorizedProblemId() < categorizedProblem.getCategorizedProblemId())
                .max(Comparator.comparing(CategorizedProblem::getCategorizedProblemId))
                .map(CategorizedProblemResponse::of)
                .orElse(null);

            CategorizedProblemResponse nextProblemResponse = problems.stream()
                .filter(cp -> cp.getCategorizedProblemId() > categorizedProblem.getCategorizedProblemId())
                .min(Comparator.comparing(CategorizedProblem::getCategorizedProblemId))
                .map(CategorizedProblemResponse::of)
                .orElse(null);

            ResponseBuilder builder = Response.builder()
                .categorizedProblemId(categorizedProblem.getCategorizedProblemId())
                .previousProblem(previousProblemResponse)
                .nextProblem(nextProblemResponse);

            Problem problem = categorizedProblem.getProblem();
            return builder.problemId(problem.getProblemId())
                .problemName(problem.getProblemName())
                .problemAnswer(problem.getProblemAnswer())
                .problemCommentary(problem.getProblemCommentary())
                .problemChoices(problem.getProblemChoices())
                .problemType(problem.getProblemType())
                .categoryName(categorizedProblem.getCategory().getCategoryName())
                .categoryId(categorizedProblem.getCategory().getCategoryId())
                .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Schema(name = "CategorizedProblemLinkedSharedResponse", description = "공유된 카테고리화 문제 응답 DTO")
    public static class LinkedSharedResponse{
        @Schema(description = "문제 응답", implementation = Response.class)
        private Response response;

        @JsonProperty("isWriter")
        @Schema(description = "작성자인지 여부", example = "true")
        private Boolean isWriter;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(name = "CategorizedProblemSimpleResponse", description = "카테고리화 문제 기본 응답 DTO")
    public static class CategorizedProblemResponse{
        @Schema(description = "카테고리화 문제 ID", example = "1")
        private Long categorizedProblemId;

        @Schema(description = "카테고리화 문제 이름", example = "샘플 문제 이름")
        private String categorizedProblemName;

        public static CategorizedProblemResponse of(CategorizedProblem categorizedProblem){
            return CategorizedProblemResponse.builder()
                .categorizedProblemId(categorizedProblem.getCategorizedProblemId())
                .categorizedProblemName(categorizedProblem.getProblem().getProblemName())
                .build();

        }
    }
}
