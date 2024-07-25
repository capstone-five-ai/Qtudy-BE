package com.app.integration.summary.membersavedsummary;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.fake.FakeSignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class MemberSavedSummaryIntegrationTest {

  @LocalServerPort
  int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Nested
  @DisplayName("요약 생성 테스트")
  class CreateSummaryTests {

    @Test
    @DisplayName("정상적으로 요약을 생성한다")
    void 요약을_생성한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

      // when
      ExtractableResponse<Response> createResponse = createSummaryRequest(jwtToken);

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedSummaryDto.Response response = createResponse.as(MemberSavedSummaryDto.Response.class);
      assertThat(response.getSummaryTitle()).isEqualTo("요약 제목");
      assertThat(response.getSummaryContent()).isEqualTo("요약 내용");
    }

    @Test
    @DisplayName("잘못된 입력으로 요약 생성 실패")
    void 잘못된_입력으로_요약_생성_실패() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

      // when
      MemberSavedSummaryDto.Post postDto = new MemberSavedSummaryDto.Post(
          "", // 요약 제목이 비어있음
          "요약 내용"
      );

      ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(postDto)
          .when().post("/api/member-saved-summary/new")
          .then().log().all()
          .extract();

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("요약 수정 테스트")
  class UpdateSummaryTests {

    @Test
    @DisplayName("정상적으로 요약을 수정한다")
    void 요약을_수정한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      ExtractableResponse<Response> createResponse = createSummaryRequest(jwtToken);
      Long summaryId = createResponse.as(MemberSavedSummaryDto.Response.class).getSummaryId();

      // when
      ExtractableResponse<Response> updateResponse = updateSummaryRequest(summaryId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedSummaryDto.Response response = updateResponse.as(MemberSavedSummaryDto.Response.class);
      assertThat(response.getSummaryTitle()).isEqualTo("새로운 요약 제목");
      assertThat(response.getSummaryContent()).isEqualTo("새로운 요약 내용");
    }

    @Test
    @DisplayName("존재하지 않는 요약을 수정 시도")
    void 존재하지_않는_요약을_수정_시도() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      Long nonExistentSummaryId = 999L;

      // when
      ExtractableResponse<Response> updateResponse = updateSummaryRequest(nonExistentSummaryId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("요약 삭제 테스트")
  class DeleteSummaryTests {

    @Test
    @DisplayName("정상적으로 요약을 삭제한다")
    void 요약을_삭제한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      ExtractableResponse<Response> createResponse = createSummaryRequest(jwtToken);
      Long summaryId = createResponse.as(MemberSavedSummaryDto.Response.class).getSummaryId();

      // when
      ExtractableResponse<Response> deleteResponse = deleteSummaryRequest(summaryId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 요약을 삭제 시도")
    void 존재하지_않는_요약을_삭제_시도() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      Long nonExistentSummaryId = 999L;

      // when
      ExtractableResponse<Response> deleteResponse = deleteSummaryRequest(nonExistentSummaryId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("요약 조회 테스트")
  class GetSummaryTests {

    @Test
    @DisplayName("정상적으로 요약을 조회한다")
    void 요약을_조회한다() {
      // given
      ExtractableResponse<Response> createResponse = createSummaryRequest(memberSignUpRequest("닉네임", "email@test.com"));
      Long summaryId = createResponse.as(MemberSavedSummaryDto.Response.class).getSummaryId();

      // when
      ExtractableResponse<Response> getResponse = getSummaryRequest(summaryId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedSummaryDto.LinkedSharedResponse response = getResponse.as(MemberSavedSummaryDto.LinkedSharedResponse.class);
      assertThat(response.getResponse().getSummaryTitle()).isEqualTo("요약 제목");
      assertThat(response.getResponse().getSummaryContent()).isEqualTo("요약 내용");
    }

    @Test
    @DisplayName("존재하지 않는 요약을 조회 시도")
    void 존재하지_않는_요약을_조회_시도() {
      // given
      Long nonExistentSummaryId = 999L;

      // when
      ExtractableResponse<Response> getResponse = getSummaryRequest(nonExistentSummaryId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("요약 PDF 다운로드 테스트")
  class DownloadSummaryPdfTests {

    @Test
    @DisplayName("정상적으로 요약 PDF를 다운로드한다")
    void 요약_PDF를_다운로드한다() throws IOException {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      ExtractableResponse<Response> createResponse = createSummaryRequest(jwtToken);
      Long summaryId = createResponse.as(MemberSavedSummaryDto.Response.class).getSummaryId();

      // when
      ExtractableResponse<Response> downloadResponse = downloadSummaryPdfRequest(summaryId, jwtToken);

      // then
      assertThat(downloadResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      assertThat(downloadResponse.header(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
      assertThat(downloadResponse.header(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment; filename=");
    }
  }

  private ExtractableResponse<Response> createSummaryRequest(String jwtToken) {
    MemberSavedSummaryDto.Post postDto = new MemberSavedSummaryDto.Post(
        "요약 제목",
        "요약 내용"
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/member-saved-summary/new")
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> updateSummaryRequest(Long summaryId, String jwtToken) {
    MemberSavedSummaryDto.Patch patchDto = new MemberSavedSummaryDto.Patch(
        "새로운 요약 제목",
        "새로운 요약 내용"
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(patchDto)
        .when().patch("/api/member-saved-summary/edit/" + summaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> deleteSummaryRequest(Long summaryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().delete("/api/member-saved-summary/delete/" + summaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> getSummaryRequest(Long summaryId) {
    return RestAssured.given().log().all()
        .when().get("/api/member-saved-summary/" + summaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> downloadSummaryPdfRequest(Long summaryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().post("/api/member-saved-summary/download-pdf/" + summaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> downloadSummaryPdfRequestWithoutAuth(Long summaryId) {
    return RestAssured.given().log().all()
        .when().post("/api/member-saved-summary/download-pdf/" + summaryId)
        .then().log().all()
        .extract();
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
