package com.app.integration.file;

import com.app.domain.file.dto.Request.DownloadPdfRequestDto;
import com.app.domain.file.dto.Request.DuplicateFileNameRequestDto;
import com.app.domain.file.dto.Request.UpdateFileRequestDto;
import com.app.domain.file.dto.Response.DownloadPdfResponseDto;
import com.app.domain.file.dto.Response.DuplicateFileNameResponseDto;
import com.app.domain.file.entity.File;
import com.app.domain.file.repository.FileRepository;
import com.app.domain.problem.aigeneratedproblem.entity.ProblemFile;
import com.app.domain.problem.aigeneratedproblem.repository.ProblemFileRepository;
import com.app.domain.summary.aigeneratedsummary.entity.SummaryFile;
import com.app.domain.summary.aigeneratedsummary.repository.SummaryFileRepository;
import com.app.global.config.ENUM.PdfType;
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

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileIntegrationTest {

    @LocalServerPort
    int port;

    @MockBean
    private FileRepository fileRepository;
    @MockBean
    private ProblemFileRepository problemFileRepository;
    @MockBean
    private SummaryFileRepository summaryFileRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        mockFileRepositoryData();
    }

    private void mockFileRepositoryData() {
        // 테스트용 파일 생성

        File mockProblemFile = new ProblemFile();
        mockProblemFile.setFileId(1L);
        mockProblemFile.setFileName("문제 파일");

        File mockSummaryFile = new SummaryFile();
        mockSummaryFile.setFileId(1L);
        mockSummaryFile.setFileName("요점 정리 파일");

        // 파일 Repository 모킹
        Mockito.when(fileRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProblemFile));
        Mockito.when(fileRepository.findByFileId(Mockito.anyLong())).thenReturn(Optional.of(mockProblemFile));
        Mockito.when(fileRepository.save(Mockito.any(File.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(fileRepository.findByFileNameAndDtype(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

        // 문제 파일 Repository 모킹
        Mockito.when(problemFileRepository.findByFileId(Mockito.anyLong())).thenReturn(Optional.of((ProblemFile) mockProblemFile));

        // 요점 정리 파일 Repository 모킹
        Mockito.when(summaryFileRepository.findByFileId(Mockito.anyLong())).thenReturn(Optional.of((SummaryFile) mockSummaryFile));
    }

    @Test
    @DisplayName("파일 이름 업데이트")
    void 파일_이름_업데이트_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        Long fileId = 1L;
        UpdateFileRequestDto updateFileRequestDto = new UpdateFileRequestDto("새 파일 이름");

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateFileRequestDto)
                .when().patch("/api/file/updateFile/" + fileId)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("Sucess", response.asString());
    }

    @Test
    @DisplayName("PDF 문제 다운로드")
    void PDF_문제_다운로드_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        Long fileId = 1L;
        DownloadPdfRequestDto downloadPdfRequestDto = new DownloadPdfRequestDto(PdfType.SUMMARY);

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(downloadPdfRequestDto)
                .when().post("/api/file/downloadPdf/" + fileId)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        DownloadPdfResponseDto responseDto = response.as(DownloadPdfResponseDto.class);
        assertNotNull(responseDto.getFileUrl());
    }

    @Test
    @DisplayName("파일 삭제")
    void 파일_삭제_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        Long fileId = 1L;

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .when().delete("/api/file/deleteFile/" + fileId)
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("Sucess", response.asString());
    }

    @Test
    @DisplayName("파일 이름 중복 확인")
    void 파일_이름_중복_확인_테스트() {
        // Given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        DuplicateFileNameRequestDto duplicateFileNameRequestDto = new DuplicateFileNameRequestDto("문제 파일", "SUMMARY");

        // When
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(duplicateFileNameRequestDto)
                .when().post("/api/file/check-duplicate")
                .then().log().all()
                .extract();

        // Then
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        DuplicateFileNameResponseDto responseDto = response.as(DuplicateFileNameResponseDto.class);
        assertNotNull(responseDto.getDuplicate());

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
