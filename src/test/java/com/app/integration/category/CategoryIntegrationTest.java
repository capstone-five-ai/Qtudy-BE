package com.app.integration.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.dto.CategoryDto.RequestDto;
import com.app.domain.category.contsant.CategoryType;
import com.app.fake.FakeSignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CategoryIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("카테고리 관리 테스트")
    class CategoryManagementTests {

        @Test
        @DisplayName("카테고리 생성")
        void 카테고리를_생성한다() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

            // when
            ExtractableResponse<Response> createResponse = categoryCreateRequest("카테고리1", CategoryType.PROBLEM, jwtToken);

            // then
            assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            CategoryDto.Response body = createResponse.as(CategoryDto.Response.class);
            assertThat(body.getCategoryName()).isEqualTo("카테고리1");
            assertThat(body.getCategoryType()).isEqualTo(CategoryType.PROBLEM);
        }

        @Test
        @DisplayName("카테고리 생성 실패 - 중복 이름")
        void 중복된_이름으로_카테고리_생성_실패() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
            categoryCreateRequest("카테고리중복", CategoryType.PROBLEM, jwtToken); // First time

            // when
            ExtractableResponse<Response> createResponse = categoryCreateRequest("카테고리중복", CategoryType.PROBLEM, jwtToken); // Second time

            // then
            assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("카테고리 수정")
        void 카테고리를_수정한다() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
            Long categoryId = createCategoryAndGetId("카테고리3", CategoryType.PROBLEM, jwtToken);

            // when
            ExtractableResponse<Response> updateResponse = categoryUpdateRequest(categoryId, "수정된 카테고리", CategoryType.PROBLEM, jwtToken);

            // then
            assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            CategoryDto.Response body = updateResponse.as(CategoryDto.Response.class);
            assertThat(body.getCategoryName()).isEqualTo("수정된 카테고리");
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 수정 실패")
        void 존재하지_않는_카테고리를_수정_시도() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
            Long nonExistentCategoryId = 9999L; // Assuming this ID doesn't exist

            // when
            ExtractableResponse<Response> updateResponse = categoryUpdateRequest(nonExistentCategoryId, "없는 카테고리", CategoryType.PROBLEM, jwtToken);

            // then
            assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("카테고리 삭제")
        void 카테고리를_삭제한다() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
            Long categoryId = createCategoryAndGetId("카테고리4", CategoryType.PROBLEM, jwtToken);

            // when
            ExtractableResponse<Response> deleteResponse = categoryDeleteRequest(categoryId, jwtToken);

            // then
            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 삭제 실패")
        void 존재하지_않는_카테고리를_삭제_시도() {
            // given
            String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
            Long nonExistentCategoryId = 9999L; // Assuming this ID doesn't exist

            // when
            ExtractableResponse<Response> deleteResponse = categoryDeleteRequest(nonExistentCategoryId, jwtToken);

            // then
            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
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

    private Long createCategoryAndGetId(String categoryName, CategoryType categoryType, String jwtToken) {
        ExtractableResponse<Response> response = categoryCreateRequest(categoryName, categoryType, jwtToken);
        return response.body().jsonPath().getLong("categoryId");
    }

    private ExtractableResponse<Response> categoryCreateRequest(String categoryName, CategoryType categoryType, String jwtToken) {
        RequestDto categoryCreateRequest = new RequestDto(categoryName, categoryType);
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(categoryCreateRequest)
            .when().post("/api/category/new")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> categoryUpdateRequest(Long categoryId, String categoryName, CategoryType categoryType, String jwtToken) {
        RequestDto categoryUpdateRequest = new RequestDto(categoryName, categoryType);
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(categoryUpdateRequest)
            .when().patch("/api/category/edit/" + categoryId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> categoryDeleteRequest(Long categoryId, String jwtToken) {
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .when().delete("/api/category/delete/" + categoryId)
            .then().log().all()
            .extract();
    }
}
