package com.app.domain.categorizedProblem.dto;

import com.app.domain.categorizedProblem.entity.CategorizedProblem;
import com.app.domain.file.problem.entity.AiGeneratedProblems;
import com.app.domain.memberSavedProblem.constant.ProblemType;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    public static class PatchResponse{
        private Long categorizedProblemId;

        private String problemName;

        private String problemAnswer;

        private String problemCommentary;

        private ProblemType problemType;

        private List<String> problemChoices;

        private String CategoryName;

        private List<CategorizedProblem> categorizedProblems;

        public static CategorizedProblemDto.PatchResponse of(CategorizedProblem categorizedProblem) {
            PatchResponseBuilder builder = PatchResponse.builder()
                    .categorizedProblemId(categorizedProblem.getCategorizedProblemId());
            if(categorizedProblem.getMemberSavedProblem() != null){
                MemberSavedProblem memberSavedProblem = categorizedProblem.getMemberSavedProblem();
                builder.problemName(memberSavedProblem.getProblemName())
                        .problemAnswer(memberSavedProblem.getProblemAnswer())
                        .problemCommentary(memberSavedProblem.getProblemCommentary())
                        .problemChoices(memberSavedProblem.getProblemChoices())
                        .problemType(memberSavedProblem.getProblemType())
                        .CategoryName(categorizedProblem.getCategory().getCategoryName())
                        .categorizedProblems(categorizedProblem.getCategory().getCategorizedProblems());
            }
            else{
                AiGeneratedProblems aiGeneratedProblems = categorizedProblem.getAiGeneratedProblems();
                builder.problemName(aiGeneratedProblems.getProblemName())
                        .problemAnswer(aiGeneratedProblems.getProblemAnswer())
                        .problemCommentary(aiGeneratedProblems.getProblemCommentary())
                        .problemChoices(aiGeneratedProblems.getProblemChoices())
                        .problemType(aiGeneratedProblems.getProblemType())
                        .CategoryName(categorizedProblem.getCategory().getCategoryName())
                        .categorizedProblems(categorizedProblem.getCategory().getCategorizedProblems());
            }
            return builder.build();
        }
    }
}
