package com.app.integration.summaryfile.aigeneratedsummary;

import com.app.domain.file.dto.Response.FileListResponseDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Request.AiGenerateSummaryDto;
import com.app.domain.summary.aigeneratedsummary.dto.SummaryFile.Response.AiGenerateSummaryResponseDto;
import com.app.global.config.ENUM.Amount;
import com.app.global.config.S3.S3Service;
import com.app.integration.dto.FakeSignUpRequest;
import com.jayway.jsonpath.TypeRef;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertFalse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SummaryFileIntegrationTest {

    @LocalServerPort
    int port;

    @MockBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("정상적으로 텍스트기반 AI 요약정리를 생성한다")
    void 텍스트기반_요약파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        AiGenerateSummaryDto aiGenerateSummaryDto = new AiGenerateSummaryDto(
                "요약할 텍스트 예시",
                Amount.FEW,
                "요약파일명");

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(aiGenerateSummaryDto)
                .when().post("/api/summaryFile/generateSummaryFileByText")
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("summaryTitle"));
    }

    @Test
    @DisplayName("이미지 기반 요약 파일 생성")
    void 이미지기반_요약파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        AiGenerateSummaryDto aiGenerateSummaryDto = new AiGenerateSummaryDto(
                "Sample Image Text",
                Amount.FEW,
                "요약파일명");

        // 테스트 이미지 파일 읽기
        ClassPathResource imageResource = new ClassPathResource("image/testImage.png");
        MockMultipartFile mockFile = null;
        try {
            mockFile = new MockMultipartFile("file", imageResource.getFilename(),
                    MediaType.IMAGE_JPEG_VALUE, imageResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // When
        ExtractableResponse<Response> response = null;
        try {
            response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("file", mockFile.getOriginalFilename(), mockFile.getBytes(), mockFile.getContentType())
                    .multiPart("text", aiGenerateSummaryDto.getText())
                    .multiPart("amount", aiGenerateSummaryDto.getAmount().toString())
                    .multiPart("fileName", aiGenerateSummaryDto.getFileName())
                    .when().post("/api/summaryFile/generateSummaryFileByImage")
                    .then().log().all()
                    .extract();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("summaryTitle"));
    }

    @Test
    @DisplayName("PDF 기반 요약 파일 생성")
    void PDF기반_요약파일_생성_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        AiGenerateSummaryDto aiGenerateSummaryDto = new AiGenerateSummaryDto("Sample PDF Text", Amount.FEW, "PDF 요약파일명");

        // PDF 파일을 src/test/resources에서 읽어오기
        ClassPathResource pdfResource = new ClassPathResource("pdf/pdf1.pdf");
        MockMultipartFile mockFile = null;
        try {
            mockFile = new MockMultipartFile("file", pdfResource.getFilename(),
                    MediaType.APPLICATION_PDF_VALUE, pdfResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // When
        ExtractableResponse<Response> response = null;
        try {
            response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("file", mockFile.getOriginalFilename(), mockFile.getBytes(), mockFile.getContentType())
                    .formParam("text", aiGenerateSummaryDto.getText())
                    .formParam("amount", aiGenerateSummaryDto.getAmount().toString())
                    .formParam("fileName", aiGenerateSummaryDto.getFileName())
                    .when().post("/api/summaryFile/generateSummaryFileByPdf")
                    .then().log().all()
                    .extract();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertNotNull(response.jsonPath().getLong("fileId"));
        assertNotNull(response.jsonPath().getString("summaryTitle"));
    }

    @Test
    @DisplayName("모든 요약 파일 리스트 조회")
    void 모든_요약파일_리스트_조회_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        int pageNumber = 1;

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .when().get("/api/summaryFile/searchAiSummaryFileList/" + pageNumber)
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
