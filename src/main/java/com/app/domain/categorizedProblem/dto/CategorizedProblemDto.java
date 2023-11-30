package com.app.domain.categorizedProblem.dto;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.category.entity.Category;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import com.app.domain.problem.entity.AiGeneratedProblem;
import com.app.global.config.ENUM.ProblemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

        private String CategoryName;

        private List<CategorizedProblemResponse> categorizedProblems;

        public static Response of(CategorizedProblem categorizedProblem) {
            ResponseBuilder builder = Response.builder()
                    .categorizedProblemId(categorizedProblem.getCategorizedProblemId());

            List<CategorizedProblemResponse> categorizedProblemResponses = categorizedProblem.getCategory().getCategorizedProblems()
                    .stream()
                    .map(CategorizedProblemResponse::of)
                    .collect(Collectors.toList());

            if(categorizedProblem.getMemberSavedProblem() != null){
                MemberSavedProblem memberSavedProblem = categorizedProblem.getMemberSavedProblem();
                builder.problemName(memberSavedProblem.getProblemName())
                        .problemAnswer(memberSavedProblem.getProblemAnswer())
                        .problemCommentary(memberSavedProblem.getProblemCommentary())
                        .problemChoices(memberSavedProblem.getProblemChoices())
                        .problemType(memberSavedProblem.getProblemType())
                        .CategoryName(categorizedProblem.getCategory().getCategoryName())
                        .categorizedProblems(categorizedProblemResponses);
            }
            else{
                AiGeneratedProblem aiGeneratedProblem = categorizedProblem.getAiGeneratedProblem();
                builder.problemName(aiGeneratedProblem.getProblemName())
                        .problemAnswer(aiGeneratedProblem.getProblemAnswer())
                        .problemCommentary(aiGeneratedProblem.getProblemCommentary())
                        .problemChoices(aiGeneratedProblem.getProblemChoices())
                        .problemType(aiGeneratedProblem.getProblemType())
                        .CategoryName(categorizedProblem.getCategory().getCategoryName())
                        .categorizedProblems(categorizedProblemResponses);
            }
            return builder.build();
        }
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
