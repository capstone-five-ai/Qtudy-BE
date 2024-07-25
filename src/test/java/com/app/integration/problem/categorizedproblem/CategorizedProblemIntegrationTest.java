package com.app.integration.problem.categorizedproblem;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.dto.CategoryDto.RequestDto;
import com.app.fake.FakeSignUpRequest;
import com.app.global.config.ENUM.ProblemType;
import com.app.domain.categorizedproblem.dto.CategorizedProblemDto;
import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
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
public class CategorizedProblemIntegrationTest {

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
  @DisplayName("Categorized Problem 생성 테스트")
  class CreateCategorizedProblemTests {

    @Test
    @DisplayName("정상적으로 카테고리화 문제를 생성한다")
    void 카테고리화_문제를_생성한다() {
      // given
      Long problemId = createProblem(jwtToken);

      // when
      ExtractableResponse<Response> createResponse = createCategorizedProblemRequest(categoryId, problemId, jwtToken);

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedProblemDto.PostResponse response = createResponse.as(CategorizedProblemDto.PostResponse.class);
      assertThat(response.getCategoryId()).containsExactly(categoryId);
      assertThat(response.getProblemId()).isEqualTo(problemId);
    }

    @Test
    @DisplayName("잘못된 입력으로 카테고리화 문제 생성 실패")
    void 잘못된_입력으로_카테고리화_문제_생성_실패() {
      // given
      Long invalidProblemId = 999L;

      // when
      CategorizedProblemDto.Post postDto = new CategorizedProblemDto.Post(
          List.of(categoryId),
          invalidProblemId
      );

      ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(postDto)
          .when().post("/api/categorized-problem/new")
          .then().log().all()
          .extract();

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Problem 수정 테스트")
  class UpdateCategorizedProblemTests {

    @Test
    @DisplayName("정상적으로 카테고리화 문제를 수정한다")
    void 카테고리화_문제를_수정한다() {
      // given
      Long problemId = createProblem(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedProblemRequest(categoryId, problemId, jwtToken);
      Long categorizedProblemId = createResponse.as(CategorizedProblemDto.PostResponse.class).getCategorizedProblemId().get(0);

      // when
      ExtractableResponse<Response> updateResponse = updateCategorizedProblemRequest(categorizedProblemId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedProblemDto.Response response = updateResponse.as(CategorizedProblemDto.Response.class);
      assertThat(response.getProblemName()).isEqualTo("수정된 문제 이름");
      assertThat(response.getProblemAnswer()).isEqualTo("수정된 정답");
      assertThat(response.getProblemCommentary()).isEqualTo("수정된 해설");
      assertThat(response.getProblemChoices()).containsExactly("수정된 옵션 A", "수정된 옵션 B");
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 문제를 수정 시도")
    void 존재하지_않는_카테고리화_문제를_수정_시도() {
      // given
      Long nonExistentCategorizedProblemId = 999L;

      // when
      ExtractableResponse<Response> updateResponse = updateCategorizedProblemRequest(nonExistentCategorizedProblemId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Problem 삭제 테스트")
  class DeleteCategorizedProblemTests {

    @Test
    @DisplayName("정상적으로 카테고리화 문제를 삭제한다")
    void 카테고리화_문제를_삭제한다() {
      // given
      Long problemId = createProblem(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedProblemRequest(categoryId, problemId, jwtToken);
      Long categorizedProblemId = createResponse.as(CategorizedProblemDto.PostResponse.class).getCategorizedProblemId().get(0);

      // when
      ExtractableResponse<Response> deleteResponse = deleteCategorizedProblemRequest(categorizedProblemId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 문제를 삭제 시도")
    void 존재하지_않는_카테고리화_문제를_삭제_시도() {
      // given
      Long nonExistentCategorizedProblemId = 999L;

      // when
      ExtractableResponse<Response> deleteResponse = deleteCategorizedProblemRequest(nonExistentCategorizedProblemId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Problem 조회 테스트")
  class GetCategorizedProblemTests {

    @Test
    @DisplayName("정상적으로 카테고리화 문제를 조회한다")
    void 카테고리화_문제를_조회한다() {
      // given
      Long problemId = createProblem(jwtToken);
      ExtractableResponse<Response> createResponse = createCategorizedProblemRequest(categoryId, problemId, jwtToken);
      Long categorizedProblemId = createResponse.as(CategorizedProblemDto.PostResponse.class).getCategorizedProblemId().get(0);

      // when
      ExtractableResponse<Response> getResponse = getCategorizedProblemRequest(categorizedProblemId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      CategorizedProblemDto.LinkedSharedResponse response = getResponse.as(CategorizedProblemDto.LinkedSharedResponse.class);
      assertThat(response.getResponse().getProblemName()).isEqualTo("문제 이름");
      assertThat(response.getResponse().getProblemAnswer()).isEqualTo("정답");
      assertThat(response.getResponse().getProblemCommentary()).isEqualTo("해설");
      assertThat(response.getResponse().getProblemType()).isEqualTo(ProblemType.MULTIPLE);
      assertThat(response.getResponse().getProblemChoices()).containsExactly("옵션 A", "옵션 B");
    }

    @Test
    @DisplayName("존재하지 않는 카테고리화 문제를 조회 시도")
    void 존재하지_않는_카테고리화_문제를_조회_시도() {
      // given
      Long nonExistentCategorizedProblemId = 999L;

      // when
      ExtractableResponse<Response> getResponse = getCategorizedProblemRequest(nonExistentCategorizedProblemId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("Categorized Problem PDF 생성 및 다운로드 테스트")
  class PdfTests {

    @Test
    @DisplayName("정상적으로 문제 PDF를 다운로드한다")
    void 문제_PDF_다운로드() {
      // given

      // when
      ExtractableResponse<Response> pdfResponse = downloadProblemPdfRequest(categoryId, jwtToken);

      // then
      assertThat(pdfResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment");
    }

    @Test
    @DisplayName("정상적으로 정답 PDF를 다운로드한다")
    void 정답_PDF_다운로드() {
      // given

      // when
      ExtractableResponse<Response> pdfResponse = downloadAnswerPdfRequest(categoryId, jwtToken);

      // then
      assertThat(pdfResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
      assertThat(pdfResponse.header(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment");
    }
  }

  private ExtractableResponse<Response> createCategorizedProblemRequest(Long categoryId, Long problemId, String jwtToken) {
    CategorizedProblemDto.Post postDto = new CategorizedProblemDto.Post(
        List.of(categoryId),
        problemId
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/categorized-problem/new")
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> updateCategorizedProblemRequest(Long categorizedProblemId, String jwtToken) {
    MemberSavedProblemDto.Patch patchDto = new MemberSavedProblemDto.Patch(
        "수정된 문제 이름",
        "수정된 정답",
        "수정된 해설",
        List.of("수정된 옵션 A", "수정된 옵션 B")
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(patchDto)
        .when().patch("/api/categorized-problem/edit/" + categorizedProblemId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> deleteCategorizedProblemRequest(Long categorizedProblemId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().delete("/api/categorized-problem/delete/" + categorizedProblemId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> getCategorizedProblemRequest(Long categorizedProblemId) {
    return RestAssured.given().log().all()
        .when().get("/api/categorized-problem/" + categorizedProblemId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> downloadProblemPdfRequest(Long categoryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().post("/api/categorized-problem/download-problem-pdf/" + categoryId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> downloadAnswerPdfRequest(Long categoryId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().post("/api/categorized-problem/download-answer-pdf/" + categoryId)
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
    CategoryType categoryType = CategoryType.PROBLEM;
    ExtractableResponse<Response> response = categoryCreateRequest(categoryName, categoryType, jwtToken);
    return response.jsonPath().getLong("categoryId");
  }

  private static Long createProblem(String jwtToken) {
    MemberSavedProblemDto.Post postDto = new MemberSavedProblemDto.Post(
        "문제 이름",
        "정답",
        "해설",
        ProblemType.MULTIPLE,
        List.of("옵션 A", "옵션 B")
    );

    ExtractableResponse<Response> response = RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/member-saved-problem/new")
        .then().log().all()
        .extract();

    return response.jsonPath().getLong("problemId");
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
