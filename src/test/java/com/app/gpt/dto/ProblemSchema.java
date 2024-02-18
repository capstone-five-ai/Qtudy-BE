package com.app.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSchema {
    @JsonPropertyDescription("의문문 형식의 문제명")
    @JsonProperty(required = true)
    String problemName;

    @JsonPropertyDescription("option의 갯수는 무조건 4개이어야 해")
    @JsonProperty(required = true)
    ProblemChoices problemChoices;

    @JsonPropertyDescription("선지에서 하나의 정답만 골라줘")
    @JsonProperty(required = true)
    String problemAnswer;

    @JsonPropertyDescription("문제의 정답에 대한 해설을 알려줘")
    @JsonProperty(required = true)
    String problemCommentary;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProblemChoices {
        @JsonPropertyDescription("The OPTION A of the question.")
        @JsonProperty(required = true)
        String one;

        @JsonPropertyDescription("The OPTION B of the question.")
        @JsonProperty(required = true)
        String two;

        @JsonPropertyDescription("The OPTION C of the question.")
        @JsonProperty(required = true)
        String three;

        @JsonPropertyDescription("The OPTION D of the question.")
        @JsonProperty(required = true)
        String four;
    }
}
