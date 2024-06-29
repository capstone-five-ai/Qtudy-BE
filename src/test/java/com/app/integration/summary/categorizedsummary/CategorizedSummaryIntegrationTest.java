package com.app.integration.summary.categorizedsummary;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.dto.CategoryDto.RequestDto;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto;
import com.app.global.config.ENUM.ProblemType;
import com.app.integration.dto.FakeSignUpRequest;
import com.app.domain.categorizedsummary.dto.CategorizedSummaryDto;
import com.app.domain.summary.dto.SummaryDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategorizedSummaryIntegrationTest {

  @LocalServerPort
  int port;

  private static Long categoryId;
  private static String jwtToken;

  @BeforeAll
  static void setUp(@LocalServerPort int port) {
    RestAssured.port = port;
    jwtToken = memberSignUpRequest("닉네임", "email@test.com");
    categoryId = createCategory(jwtToken);
  }

  @Nested
  @DisplayName("Categorized Summary 생성 테스트")
  class CreateCategorizedSummaryTests {

    @Test
    @DisplayName("정상적으로 카테고리화 요약을 생성한다")
    void 카테고리화_요약을_생성한다() {
      // given
      Long summaryId = createSummary(jwtToken);

      // when
      ExtractableResponse<Response> createResponse = createCategorizedSummaryRequest(categoryId, summaryId, jwtToken);

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedSummaryDto.PostResponse response = createResponse.as(CategorizedSummaryDto.PostResponse.class);
      assertThat(response.getCategoryId()).containsExactly(categoryId);
      assertThat(response.getSummaryId()).isEqualTo(summaryId);
    }

    @Test
    @DisplayName("잘못된 입력으로 카테고리화 요약 생성 실패")
    void 잘못된_입력으로_카테고리화_요약_생성_실패() {
      // given
      Long invalidSummaryId = 999L;

      // when
      CategorizedSummaryDto.Post postDto = new CategorizedSummaryDto.Post(
          List.of(categoryId),
          invalidSummaryId
      );

      ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(postDto)
          .when().post("/api/categorized-summary/new")
          .then().log().all()
          .extract();

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Summary 수정 테스트")
  class UpdateCategorizedSummaryTests {

    @Test
    @DisplayName("정상적으로 카테고리화 요약을 수정한다")
    void 카테고리화_요약을_수정한다() {
      // given
      Long summaryId = createSummary(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedSummaryRequest(categoryId, summaryId, jwtToken);
      Long categorizedSummaryId = createResponse.as(CategorizedSummaryDto.PostResponse.class).getCategorizedSummaryId().get(0);

      // when
      ExtractableResponse<Response> updateResponse = updateCategorizedSummaryRequest(categorizedSummaryId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedSummaryDto.Response response = updateResponse.as(CategorizedSummaryDto.Response.class);
      assertThat(response.getSummaryTitle()).isEqualTo("수정된 요약 제목");
      assertThat(response.getSummaryContent()).isEqualTo("수정된 요약 내용");
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 요약을 수정 시도")
    void 존재하지_않는_카테고리화_요약을_수정_시도() {
      // given
      Long nonExistentCategorizedSummaryId = 999L;

      // when
      ExtractableResponse<Response> updateResponse = updateCategorizedSummaryRequest(nonExistentCategorizedSummaryId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Summary 삭제 테스트")
  class DeleteCategorizedSummaryTests {

    @Test
    @DisplayName("정상적으로 카테고리화 요약을 삭제한다")
    void 카테고리화_요약을_삭제한다() {
      // given
      Long summaryId = createSummary(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedSummaryRequest(categoryId, summaryId, jwtToken);
      Long categorizedSummaryId = createResponse.as(CategorizedSummaryDto.PostResponse.class).getCategorizedSummaryId().get(0);

      // when
      ExtractableResponse<Response> deleteResponse = deleteCategorizedSummaryRequest(categorizedSummaryId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 요약을 삭제 시도")
    void 존재하지_않는_카테고리화_요약을_삭제_시도() {
      // given
      Long nonExistentCategorizedSummaryId = 999L;

      // when
      ExtractableResponse<Response> deleteResponse = deleteCategorizedSummaryRequest(nonExistentCategorizedSummaryId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Summary 조회 테스트")
  class GetCategorizedSummaryTests {

    @Test
    @DisplayName("정상적으로 카테고리화 요약을 조회한다")
    void 카테고리화_요약을_조회한다() {
      // given
      Long summaryId = createSummary(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedSummaryRequest(categoryId, summaryId, jwtToken);
      Long categorizedSummaryId = createResponse.as(CategorizedSummaryDto.PostResponse.class).getCategorizedSummaryId().get(0);

      // when
      ExtractableResponse<Response> getResponse = getCategorizedSummaryRequest(categorizedSummaryId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedSummaryDto.LinkedSharedResponse response = getResponse.as(CategorizedSummaryDto.LinkedSharedResponse.class);
      assertThat(response.getResponse().getSummaryTitle()).isEqualTo("요약 제목");
      assertThat(response.getResponse().getSummaryContent()).isEqualTo("요약 내용");
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 요약을 조회 시도")
    void 존재하지_않는_카테고리화_요약을_조회_시도() {
      // given
      Long nonExistentCategorizedSummaryId = 999L;

      // when
      ExtractableResponse<Response> getResponse = getCategorizedSummaryRequest(nonExistentCategorizedSummaryId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Summary PDF 생성 및 다운로드 테스트")
  class PdfTests {

    @Test
    @DisplayName("정상적으로 요약 PDF를 다운로드한다")
    void 요약_PDF_다운로드() {
      // given
      Long summaryId = createSummary(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedSummaryRequest(categoryId, summaryId, jwtToken);
      Long categorizedSummaryId = createResponse.as(CategorizedSummaryDto.PostResponse.class).getCategorizedSummaryId().get(0);

      // when
      ExtractableResponse<Response> pdfResponse = downloadSummaryPdfRequest(categorizedSummaryId, jwtToken);

      // then
      assertThat(pdfResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment");
    }
  }

  private ExtractableResponse<Response> createCategorizedSummaryRequest(Long categoryId, Long summaryId, String jwtToken) {
    CategorizedSummaryDto.Post postDto = new CategorizedSummaryDto.Post(
        List.of(categoryId),
        summaryId
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/categorized-summary/new")
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> updateCategorizedSummaryRequest(Long categorizedSummaryId, String jwtToken) {
    SummaryDto.Patch patchDto = new SummaryDto.Patch(
        "수정된 요약 제목",
        "수정된 요약 내용"
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(patchDto)
        .when().patch("/api/categorized-summary/edit/" + categorizedSummaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> deleteCategorizedSummaryRequest(Long categorizedSummaryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().delete("/api/categorized-summary/delete/" + categorizedSummaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> getCategorizedSummaryRequest(Long categorizedSummaryId) {
    return RestAssured.given().log().all()
        .when().get("/api/categorized-summary/" + categorizedSummaryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> downloadSummaryPdfRequest(Long categorizedSummaryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().post("/api/categorized-summary/download-pdf/" + categorizedSummaryId)
        .then().log().all()
        .extract();
  }

  private static String memberSignUpRequest(String nickname, String email) {
    FakeSignUpRequest signUpRequest = new FakeSignUpRequest(nickname, email);
    ExtractableResponse<Response> signUpResponse = RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(signUpRequest)
        .when().post("/api/fake/login")
        .then().log().all()
        .extract();
    return signUpResponse.asString();
  }

  private static Long createCategory(String jwtToken) {
    String categoryName = "카테고리 이름";
    CategoryType categoryType = CategoryType.SUMMARY;
    ExtractableResponse<Response> response = categoryCreateRequest(categoryName, categoryType, jwtToken);
    return response.jsonPath().getLong("categoryId");
  }

  private static Long createSummary(String jwtToken) {
    MemberSavedSummaryDto.Post postDto = new MemberSavedSummaryDto.Post(
        "요약 제목",
        "요약 내용"
    );

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/member-saved-summary/new")
        .then().log().all()
        .extract();

    return response.jsonPath().getLong("summaryId");
  }



  private static ExtractableResponse<Response> categoryCreateRequest(String categoryName, CategoryType categoryType, String jwtToken) {
    RequestDto categoryCreateRequest = new RequestDto(categoryName, categoryType);
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(categoryCreateRequest)
        .when().post("/api/category/new")
        .then().log().all()
        .extract();
  }
}
