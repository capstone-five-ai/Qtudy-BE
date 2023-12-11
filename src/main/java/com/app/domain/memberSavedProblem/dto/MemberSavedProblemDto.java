package com.app.domain.memberSavedProblem.dto;

import com.app.global.config.ENUM.ProblemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MemberSavedProblemDto {

    @Getter
    public static class Post{

        @NotBlank
        private String problemName;

        private String problemAnswer;

        private String problemCommentary;

        private ProblemType problemType;

        private List<String> problemChoices;
    }

    @Getter
    public static class Patch{
        private String problemName;

        private String problemAnswer;

        private String problemCommentary;

        private List<String> problemChoices;
    }
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long memberSavedProblemId;

        private String problemName;

        private String problemAnswer;

        private String problemCommentary;

        private ProblemType problemType;

        private List<String> problemChoices;

    }


    public static class LinkSharedResponse{
        private Response response;

        @JsonProperty("isWriter")
        private boolean isWriter;

        public Response getResponse() {
            return response;
        }

        public boolean getisWriter() {
            return isWriter;
        }

        public LinkSharedResponse(Response response, boolean isWriter) {
            this.response = response;
            this.isWriter = isWriter;
        }
    }

}
