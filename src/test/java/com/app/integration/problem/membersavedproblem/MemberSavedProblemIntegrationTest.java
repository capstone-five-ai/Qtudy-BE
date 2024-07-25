package com.app.integration.problem.membersavedproblem;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.fake.FakeSignUpRequest;
import com.app.global.config.ENUM.ProblemType;
import com.app.domain.problem.membersavedproblem.dto.MemberSavedProblemDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
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
public class MemberSavedProblemIntegrationTest {

  @LocalServerPort
  int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Nested
  @DisplayName("문제 생성 테스트")
  class CreateProblemTests {

    @Test
    @DisplayName("정상적으로 문제를 생성한다")
    void 문제를_생성한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

      // when
      ExtractableResponse<Response> createResponse = createProblemRequest(jwtToken);

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedProblemDto.Response response = createResponse.as(MemberSavedProblemDto.Response.class);
      assertThat(response.getProblemName()).isEqualTo("문제 이름");
      assertThat(response.getProblemAnswer()).isEqualTo("정답");
      assertThat(response.getProblemCommentary()).isEqualTo("해설");
      assertThat(response.getProblemType()).isEqualTo(ProblemType.MULTIPLE);
      assertThat(response.getProblemChoices()).containsExactly("옵션 A", "옵션 B");
    }

    @Test
    @DisplayName("잘못된 입력으로 문제 생성 실패")
    void 잘못된_입력으로_문제_생성_실패() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

      // when
      MemberSavedProblemDto.Post postDto = new MemberSavedProblemDto.Post(
          "", // 문제 이름이 비어있음
          "정답",
          "해설",
          ProblemType.MULTIPLE,
          List.of("옵션 A", "옵션 B")
      );

      ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(postDto)
          .when().post("/api/member-saved-problem/new")
          .then().log().all()
          .extract();

      // then
      assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("문제 수정 테스트")
  class UpdateProblemTests {

    @Test
    @DisplayName("정상적으로 문제를 수정한다")
    void 문제를_수정한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      ExtractableResponse<Response> createResponse = createProblemRequest(jwtToken);
      Long problemId = createResponse.as(MemberSavedProblemDto.Response.class).getProblemId();

      // when
      ExtractableResponse<Response> updateResponse = updateProblemRequest(problemId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedProblemDto.Response response = updateResponse.as(MemberSavedProblemDto.Response.class);
      assertThat(response.getProblemName()).isEqualTo("수정된 문제 이름");
      assertThat(response.getProblemAnswer()).isEqualTo("수정된 정답");
      assertThat(response.getProblemCommentary()).isEqualTo("수정된 해설");
      assertThat(response.getProblemChoices()).containsExactly("수정된 옵션 A", "수정된 옵션 B");
    }

    @Test
    @DisplayName("존재하지 않는 문제를 수정 시도")
    void 존재하지_않는_문제를_수정_시도() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      Long nonExistentProblemId = 999L;

      // when
      ExtractableResponse<Response> updateResponse = updateProblemRequest(nonExistentProblemId, jwtToken);

      // then
      assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("문제 삭제 테스트")
  class DeleteProblemTests {

    @Test
    @DisplayName("정상적으로 문제를 삭제한다")
    void 문제를_삭제한다() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      ExtractableResponse<Response> createResponse = createProblemRequest(jwtToken);
      Long problemId = createResponse.as(MemberSavedProblemDto.Response.class).getProblemId();

      // when
      ExtractableResponse<Response> deleteResponse = deleteProblemRequest(problemId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 문제를 삭제 시도")
    void 존재하지_않는_문제를_삭제_시도() {
      // given
      String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
      Long nonExistentProblemId = 999L;

      // when
      ExtractableResponse<Response> deleteResponse = deleteProblemRequest(nonExistentProblemId, jwtToken);

      // then
      assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  @Nested
  @DisplayName("문제 조회 테스트")
  class GetProblemTests {

    @Test
    @DisplayName("정상적으로 문제를 조회한다")
    void 문제를_조회한다() {
      // given
      ExtractableResponse<Response> createResponse = createProblemRequest(memberSignUpRequest("닉네임", "email@test.com"));
      Long problemId = createResponse.as(MemberSavedProblemDto.Response.class).getProblemId();

      // when
      ExtractableResponse<Response> getResponse = getProblemRequest(problemId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
      MemberSavedProblemDto.LinkSharedResponse response = getResponse.as(MemberSavedProblemDto.LinkSharedResponse.class);
      assertThat(response.getResponse().getProblemName()).isEqualTo("문제 이름");
      assertThat(response.getResponse().getProblemAnswer()).isEqualTo("정답");
      assertThat(response.getResponse().getProblemCommentary()).isEqualTo("해설");
      assertThat(response.getResponse().getProblemType()).isEqualTo(ProblemType.MULTIPLE);
      assertThat(response.getResponse().getProblemChoices()).containsExactly("옵션 A", "옵션 B");
    }

    @Test
    @DisplayName("존재하지 않는 문제를 조회 시도")
    void 존재하지_않는_문제를_조회_시도() {
      // given
      Long nonExistentProblemId = 999L;

      // when
      ExtractableResponse<Response> getResponse = getProblemRequest(nonExistentProblemId);

      // then
      assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
  }

  private ExtractableResponse<Response> createProblemRequest(String jwtToken) {
    MemberSavedProblemDto.Post postDto = new MemberSavedProblemDto.Post(
        "문제 이름",
        "정답",
        "해설",
        ProblemType.MULTIPLE,
        List.of("옵션 A", "옵션 B")
    );

    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(postDto)
        .when().post("/api/member-saved-problem/new")
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> updateProblemRequest(Long problemId, String jwtToken) {
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
        .when().patch("/api/member-saved-problem/edit/" + problemId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> deleteProblemRequest(Long problemId, String jwtToken) {
    return RestAssured.given().log().all()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
        .when().delete("/api/member-saved-problem/delete/" + problemId)
        .then().log().all()
        .extract();
  }

  private ExtractableResponse<Response> getProblemRequest(Long problemId) {
    return RestAssured.given().log().all()
        .when().get("/api/member-saved-problem/" + problemId)
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
