package es.upm.miw.functionaltests.tpv.user;

import es.upm.miw.functionaltests.HttpRequestBuilder;
import es.upm.miw.functionaltests.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserResourceFT {

    static final String URL = "http://localhost:8080/tpv-user/users";
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;

    @Test
    void testCreateUser() {
        UserDto userDto = UserDto.builder().mobile("666666001").firstName("test").password("test").dni(null).address("C/TPV, 0").build();
        ResponseEntity<UserDto> response = httpRequestBuilder.post(URL).role(Role.URL_TOKEN).body(userDto).exchange(UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testReadByMobile() {
        ResponseEntity<UserDto> response = this.httpRequestBuilder
                .get(URL + "/mobile/{mobile}", "66").role(Role.ADMIN).exchange(UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMobile()).isEqualTo("66");
        assertThat(response.getBody().getFirstName()).isEqualTo("customer");
    }

    @Test
    void testFindAllWithAdmin() {
        ResponseEntity<UserDto[]> response = httpRequestBuilder.get(URL).role(Role.ADMIN).exchange(UserDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(Arrays.stream(response.getBody()).map(UserDto::getFirstName).toList())
                .contains("admin");
    }

    @Test
    void testFindAllWithManager() {
        ResponseEntity<UserDto[]> response = httpRequestBuilder.get(URL).role(Role.MANAGER).exchange(UserDto[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        assertThat(Arrays.stream(response.getBody()).map(UserDto::getFirstName).toList())
                .contains("man")
                .doesNotContain("admin");
    }

}
