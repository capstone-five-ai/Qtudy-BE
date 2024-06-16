package com.app.integration.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.domain.category.contsant.CategoryType;
import com.app.domain.category.dto.CategoryDto;
import com.app.domain.category.dto.CategoryDto.RequestDto;
import com.app.integration.dto.FakeSignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void 카테고리를_생성한다() {
        // given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");

        // when
        ExtractableResponse<Response> categoryCreateResponse = categoryCreateRequest(
                "카테고리1",
                CategoryType.PROBLEM,
                jwtToken
        );
        CategoryDto.Response body = categoryCreateResponse.as(CategoryDto.Response.class);

        // then
        assertThat(categoryCreateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getCategoryName()).isEqualTo("카테고리1");
        assertThat(body.getCategoryType()).isEqualTo(CategoryType.PROBLEM);
    }

    @Test
    void 생성한_카테고리를_단건_조회한다() {
        // given
        String jwtToken = memberSignUpRequest("닉네임", "email@test.com");
        ExtractableResponse<Response> categoryCreateResponse = categoryCreateRequest(
                "카테고리2",
                CategoryType.PROBLEM,
                jwtToken
        );
        Long categoryId = categoryCreateResponse.as(CategoryDto.Response.class).getCategoryId();

        // when
        ExtractableResponse<Response> categoryFindResponse = RestAssured.given().log().all()
                .when().get("/api/category/{categoryId}", categoryId)
                .then().log().all()
                .extract();

        // then
        assertThat(categoryFindResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> categoryCreateRequest(
            String categoryName,
            CategoryType categoryType,
            String jwtToken
    ) {
        RequestDto categoryCreateRequest = new RequestDto(categoryName, categoryType);
        ExtractableResponse<Response> categoryCreateResponse = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(categoryCreateRequest)
                .when().post("/api/category/new")
                .then().log().all()
                .extract();
        return categoryCreateResponse;
    }

    private String memberSignUpRequest(String ninckname, String email) {
        FakeSignUpRequest signUpRequest = new FakeSignUpRequest(ninckname, email);
        ExtractableResponse<Response> signUpResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signUpRequest)
                .when().post("/api/fake/login")
                .then().log().all()
                .extract();
        return signUpResponse.asString();
    }
}
