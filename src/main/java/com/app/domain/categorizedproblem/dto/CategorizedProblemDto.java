package com.app.domain.categorizedproblem.dto;

import com.app.domain.categorizedproblem.entity.CategorizedProblem;
import com.app.domain.problem.membersavedproblem.entity.MemberSavedProblem;
import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
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

        private Long memberSavedProblemId;

        private Integer aiGeneratedProblemId;
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

        private Long memberSavedProblemId;

        private Integer aiGeneratedProblemId;
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

        private CategorizedProblemResponse previousProblem;

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

            if(categorizedProblem.getMemberSavedProblem() != null){
                MemberSavedProblem memberSavedProblem = categorizedProblem.getMemberSavedProblem();
                builder.problemName(memberSavedProblem.getProblemName())
                        .problemAnswer(memberSavedProblem.getProblemAnswer())
                        .problemCommentary(memberSavedProblem.getProblemCommentary())
                        .problemChoices(memberSavedProblem.getProblemChoices())
                        .problemType(memberSavedProblem.getProblemType())
                        .categoryName(categorizedProblem.getCategory().getCategoryName())
                        .categoryId(categorizedProblem.getCategory().getCategoryId());
            }
            else{
                AiGeneratedProblem aiGeneratedProblem = categorizedProblem.getAiGeneratedProblem();
                builder.problemName(aiGeneratedProblem.getProblemName())
                        .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                        .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                        .problemChoices(aiGeneratedProblem.getProblemChoices())
                        .problemType(aiGeneratedProblem.getProblemType())
                        .categoryName(categorizedProblem.getCategory().getCategoryName())
                        .categoryId(categorizedProblem.getCategory().getCategoryId());
            }
            return builder.build();
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
                    .categorizedProblemName(categorizedProblem.getMemberSavedProblem() != null ?
                            categorizedProblem.getMemberSavedProblem().getProblemName() :
                            categorizedProblem.getAiGeneratedProblem().getProblemName())
                    .build();

        }
    }
}
