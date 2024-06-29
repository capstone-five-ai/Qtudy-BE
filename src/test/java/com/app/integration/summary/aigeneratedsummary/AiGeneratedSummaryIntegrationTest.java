package com.app.integration.summary.aigeneratedsummary;
import com.app.domain.summary.aigeneratedsummary.dto.Summary.Response.SummaryResponseDto;
import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import com.app.domain.summary.aigeneratedsummary.entity.SummaryFile;
import com.app.domain.summary.aigeneratedsummary.service.AiGeneratedSummaryService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AiGeneratedSummaryIntegrationTest {

    @LocalServerPort
    int port;

    @MockBean
    private AiGeneratedSummaryService aiGeneratedSummaryService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // Mocking된 SummaryFile 객체 정의
        SummaryFile mockSummaryFile = new SummaryFile();
        mockSummaryFile.setFileId(1L); // 필요한 경우 ID 설정

        // Mocking된 AiGeneratedSummary 객체 정의
        AiGeneratedSummary mockSummary = AiGeneratedSummary.builder()
                .summaryId(1L)
                .summaryTitle("Sample Summary Title")
                .summaryContent("This is a sample summary content.")
                .summaryFile(mockSummaryFile)
                .build();

        // Mocking된 AiGeneratedSummaryService의 동작 정의
        Mockito.when(aiGeneratedSummaryService.GetSummary(Mockito.anyLong())).thenReturn(mockSummary);
        Mockito.when(aiGeneratedSummaryService.checkIsWriter(Mockito.any(HttpServletRequest.class), Mockito.anyLong())).thenReturn(true);
    }

    @Test
    @DisplayName("사용자가 생성한 AI 요약을 조회한다.")
    void AI_요약_조회하기_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        long fileId = 1L;

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .when().get("/api/summary/getSummary/" + fileId)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        SummaryResponseDto responseDto = response.as(SummaryResponseDto.class);
        assertEquals("Sample Summary Title", responseDto.getResponse().getSummaryTitle());
        assertEquals("This is a sample summary content.", responseDto.getResponse().getSummaryContent());
        assertTrue(responseDto.getIsWriter());
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

