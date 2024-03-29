package com.app.domain.categorizedproblem.dto;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.problem.entity.Problem;
import com.app.global.config.ENUM.ProblemType;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class Post {

        private List<Long> categoryIdList;

        private Long problemId;
    }

    @Getter
    public static class Patch{
        private String problemName;

        private String problemAnswer;

        private String problemCommentarty;

        private List<String> problemChoices;
    }

    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private List<Long> categorizedProblemId;
        private List<Long> categoryId;

        private Long problemId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long categorizedProblemId;

        private String problemName;

        private String problemAnswer;

        private String problemCommentary;

        private ProblemType problemType;

        private List<String> problemChoices;

        private String categoryName;

        private Long categoryId;

        private List<CategorizedProblemResponse> categorizedProblems;

        public static Response of(CategorizedProblem categorizedProblem) {

            List<CategorizedProblem> problems = categorizedProblem.getCategory().getCategorizedProblems();
            CategorizedProblem previousProblem = problems.stream()
                    .filter(cp -> cp.getCategorizedProblemId() < categorizedProblem.getCategorizedProblemId())
                    .max(Comparator.comparing(CategorizedProblem::getCategorizedProblemId))
                    .orElse(null);

            CategorizedProblem nextProblem = problems.stream()
                    .filter(cp -> cp.getCategorizedProblemId() > categorizedProblem.getCategorizedProblemId())
                    .min(Comparator.comparing(CategorizedProblem::getCategorizedProblemId))
                    .orElse(null);


            List<CategorizedProblemResponse> categorizedProblemResponses = Stream.of(previousProblem, nextProblem)
                    .map(CategorizedProblemResponse::of)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            ResponseBuilder builder = Response.builder()
                    .categorizedProblemId(categorizedProblem.getCategorizedProblemId());

            Problem problem = categorizedProblem.getProblem();
            return builder.problemName(problem.getProblemName())
                    .problemAnswer(problem.getProblemAnswer())
                    .problemCommentary(problem.getProblemCommentary())
                    .problemChoices(problem.getProblemChoices())
                    .problemType(problem.getProblemType())
                    .categoryName(categorizedProblem.getCategory().getCategoryName())
                    .categoryId(categorizedProblem.getCategory().getCategoryId())
                    .categorizedProblems(categorizedProblemResponses)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class LinkedSharedResponse{
        private Response response;

        @JsonProperty("isWriter")
        private Boolean isWriter;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategorizedProblemResponse{
        private Long categorizedProblemId;

        private String categorizedProblemName;

        public static CategorizedProblemResponse of(CategorizedProblem categorizedProblem){
            return CategorizedProblemResponse.builder()
                    .categorizedProblemId(categorizedProblem.getCategorizedProblemId())
                    .categorizedProblemName(categorizedProblem.getProblem().getProblemName())
                    .build();

        }
    }
}
