package com.app.integration.problemfile;

import com.app.domain.problem.aigeneratedproblem.dto.ProblemFile.Request.AiGenerateProblemDto;
import com.app.fake.FakeSignUpRequest;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.ENUM.ProblemDifficulty;
import com.app.global.config.ENUM.ProblemType;
import com.app.global.config.S3.S3Service;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProblemFileIntegrationTest {

    @LocalServerPort
    int port;

    @MockBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("정상적으로 텍스트기반 AI 문제를 생성한다.")
    void 텍스트기반_문제파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

        AiGenerateProblemDto aiGenerateProblemDto = new AiGenerateProblemDto(
                "텍스트",
                ProblemType.MULTIPLE,
                Amount.FEW,
                ProblemDifficulty.EASY,
                "파일 이름");

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(aiGenerateProblemDto)
                .when().post("/api/problemFile/generateProblemFileByText")
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("problems"));
    }

    @Test
    @DisplayName("정상적으로 이미지기반 AI 문제를 생성한다.")
    void 이미지기반_문제파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

        AiGenerateProblemDto aiGenerateProblemDto = new AiGenerateProblemDto(
                "텍스트",
                ProblemType.MULTIPLE,
                Amount.FEW,
                ProblemDifficulty.EASY,
                "파일 이름");

        // 테스트 이미지 파일 읽기
        ClassPathResource imageResource = new ClassPathResource("image/testImage.png");
        MockMultipartFile mockFile;
        try {
            mockFile = new MockMultipartFile("file", imageResource.getFilename(),
                    MediaType.IMAGE_JPEG_VALUE, imageResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // When
        ExtractableResponse<Response> response;
        try {
            response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("file", mockFile.getOriginalFilename(), mockFile.getBytes(), mockFile.getContentType())
                    .formParam("type", aiGenerateProblemDto.getType())
                    .formParam("text", aiGenerateProblemDto.getText())
                    .formParam("difficulty", aiGenerateProblemDto.getDifficulty().toString())
                    .formParam("amount", aiGenerateProblemDto.getAmount().toString())
                    .formParam("fileName", aiGenerateProblemDto.getFileName())
                    .when().post("/api/problemFile/generateProblemFileByImage")
                    .then().log().all()
                    .extract();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("problems"));
    }

    @Test
    @DisplayName("정상적으로 PDF기반 AI 문제를 생성한다.")
    void PDF기반_문제파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

        AiGenerateProblemDto aiGenerateProblemDto = new AiGenerateProblemDto(
                "텍스트",
                ProblemType.MULTIPLE,
                Amount.FEW,
                ProblemDifficulty.EASY,
                "PDF 파일 이름");

        // 테스트 PDF 파일 읽기
        ClassPathResource pdfResource = new ClassPathResource("pdf/pdf1.pdf");
        MockMultipartFile mockFile;
        try {
            mockFile = new MockMultipartFile("file", pdfResource.getFilename(),
                    MediaType.APPLICATION_PDF_VALUE, pdfResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // When
        ExtractableResponse<Response> response;
        try {
            response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("file", mockFile.getOriginalFilename(), mockFile.getBytes(), mockFile.getContentType())
                    .formParam("type", aiGenerateProblemDto.getType())
                    .formParam("text", aiGenerateProblemDto.getText())
                    .formParam("difficulty", aiGenerateProblemDto.getDifficulty().toString())
                    .formParam("amount", aiGenerateProblemDto.getAmount().toString())
                    .formParam("fileName", aiGenerateProblemDto.getFileName())
                    .when().post("/api/problemFile/generateProblemFileByPdf")
                    .then().log().all()
                    .extract();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("problems"));
    }

    @Test
    @DisplayName("정상적으로 사용자가 생성한 모든 AI 파일을 조회한다.")
    void 모든_AI_문제파일_조회하기_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        int pageNumber = 1;

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .when().get("/api/problemFile/searchAiProblemFileList/" + pageNumber)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        // 응답 본문에 따른 추가 확인 사항을 여기에 작성하세요
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
