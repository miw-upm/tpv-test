package es.upm.miw.functionaltests.tpv.user;

import es.upm.miw.functionaltests.HttpRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserResourceFT {

    public static final String URL = "http://localhost:8080/tpv-user/users";
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;

    @Test
    void testCreateUser() {
        UserDto userDto = UserDto.builder().mobile("666666001").firstName("test").password("test").dni(null).address("C/TPV, 0").build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto, headers);

        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                URL,
                HttpMethod.POST,
                entity,
                UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

}
