package com.app.integration.problem.aigeneratedproblem;

import com.app.domain.problem.aigeneratedproblem.entity.AiGeneratedProblem;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.domain.problem.aigeneratedproblem.repository.AiGeneratedProblemRepository;
import com.app.domain.problem.aigeneratedproblem.repository.ProblemFileRepository;
import com.app.domain.problem.aigeneratedproblem.service.AiGeneratedProblemService;
import com.app.global.config.ENUM.ProblemType;
import com.app.integration.dto.FakeSignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AiGeneratedProblemIntegrationTest {

    @LocalServerPort
    int port;

    @MockBean
    private ProblemFileRepository problemFileRepository;

    @MockBean
    private AiGeneratedProblemRepository aiGeneratedProblemRepository;

    @MockBean
    private AiGeneratedProblemService aiGeneratedProblemService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Mocking된 ProblemFile 객체 정의
        ProblemFile mockProblemFile = new ProblemFile();
        mockProblemFile.setFileId(1L); // 필요한 경우 ID 설정

        // Mocking된 AiGeneratedProblem 리스트 정의
        List<AiGeneratedProblem> mockProblems = Arrays.asList(
                AiGeneratedProblem.builder()
                        .problemName("Sample Problem 1")
                        .problemAnswer("Answer 1")
                        .problemCommentary("Commentary 1")
                        .problemType(ProblemType.MULTIPLE)
                        .problemChoices(Arrays.asList("Choice 1", "Choice 2"))
                        .problemFile(mockProblemFile)
                        .build()
        );

        // Mocking된 ProblemFileRepository와 AiGeneratedProblemRepository의 동작 정의
        Mockito.when(problemFileRepository.getByFileId(Mockito.anyLong())).thenReturn(mockProblemFile);
        Mockito.when(aiGeneratedProblemRepository.findByProblemFile_FileId(Mockito.anyLong())).thenReturn(mockProblems);

        // checkIsWriter 메서드 Mocking
        Mockito.when(aiGeneratedProblemService.checkIsWriter(Mockito.any(HttpServletRequest.class), Mockito.anyLong())).thenReturn(true);
    }

    @Test
    @DisplayName("사용자가 생성한 AI 문제의 문제 리스트를 조회한다.")
    void AI_문제_리스트_조회하기_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        long fileId = 1L;

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .when().get("/api/problem/getFileProblems/" + fileId)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    private String memberSignUpRequest(String nickname, String email) {
        FakeSignUpRequest signUpRequest = new FakeSignUpRequest(nickname, email);
        ExtractableResponse<Response> signUpResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signUpRequest)
                .when().post("/api/fake/login")
                .then().log().all()
                .extract();
        return signUpResponse.asString();
    }
}
