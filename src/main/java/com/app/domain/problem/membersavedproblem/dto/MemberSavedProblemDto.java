package com.app.domain.problem.membersavedproblem.dto;

import com.app.global.config.ENUM.ProblemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MemberSavedProblemDto {

    @Getter
    @Schema(name = "MemberSavedProblemPost", description = "새 문제 생성 요청 DTO")
    public static class Post {

        @NotBlank
        @Schema(description = "문제 이름", example = "이름 예시")
        private String problemName;

        @Schema(description = "문제 정답", example = "2")
        private String problemAnswer;

        @Schema(description = "문제 해설", example = "문제 해설 예시")
        private String problemCommentary;

        @Schema(description = "문제 유형", example = "MULTIPLE")
        private ProblemType problemType;

        @Schema(description = "문제 선지", example = "[\"옵션 A\", \"옵션 B\"]")
        private List<String> problemChoices;
    }

    @Getter
    @Schema(name = "MemberSavedProblemPatch", description = "문제 수정 요청 DTO")
    public static class Patch {
        @Schema(description = "문제의 이름", example = "수정된 문제 이름")
        private String problemName;

        @Schema(description = "문제의 정답", example = "수정된 정답")
        private String problemAnswer;

        @Schema(description = "문제 해설", example = "수정된 해설")
        private String problemCommentary;

        @Schema(description = "선택지 목록", example = "[\"수정된 옵션 A\", \"수정된 옵션 B\"]")
        private List<String> problemChoices;
    }

    @Getter
    @AllArgsConstructor
    @Schema(name = "MemberSavedProblemResponse", description = "문제 응답 DTO")
    public static class Response {
        @Schema(description = "문제 ID", example = "1")
        private Long problemId;

        @Schema(description = "문제의 이름", example = "샘플 문제")
        private String problemName;

        @Schema(description = "문제의 정답", example = "정답 예시")
        private String problemAnswer;

        @Schema(description = "문제 해설", example = "이것은 샘플 해설입니다.")
        private String problemCommentary;

        @Schema(description = "문제 유형", example = "다중 선택")
        private ProblemType problemType;

        @Schema(description = "선택지 목록", example = "[\"옵션 A\", \"옵션 B\"]")
        private List<String> problemChoices;
    }

    @Schema(name = "MemberSavedProblemLinkSharedResponse", description = "공유된 문제 응답 DTO")
    public static class LinkSharedResponse {
        private Response response;

        @JsonProperty("isWriter")
        @Schema(description = "작성자인지 여부", example = "true")
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
