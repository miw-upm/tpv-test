package es.upm.miw.functionaltests.tpv.article;

import es.upm.miw.functionaltests.HttpRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static es.upm.miw.functionaltests.Scope.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProviderResourceFT {

    public static final String URL = "http://localhost:8080/tpv-article/providers";
    @Autowired
    private HttpRequestBuilder httpRequestBuilder;

    @Test
    void testReadProvider() {
        ResponseEntity<Provider> response = this.httpRequestBuilder
                .get(URL + "/{company}", "pro1").exchange(Provider.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting(Provider::getCompany, Provider::getNif, Provider::getPhone, Provider::getAddress,
                        Provider::getEmail, Provider::getNote, Provider::getActive)
                .containsExactly("pro1", "12345678b", "9166666601", "C/TPV-pro, 1", "p1@gmail.com", "p1", true);
    }

    @Test
    void testReadProviderNotFound() {
        ResponseEntity<Provider> response = this.httpRequestBuilder
                .get(URL + "/{company}", "KK").scope(ADMIN).exchange(Provider.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindProviders() {
        ResponseEntity<ProviderCompanyDto> response = this.httpRequestBuilder
                .get(URL + "/companies").scope(ADMIN).param("company","p").exchange(ProviderCompanyDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCompanies()).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void testReadProviderUnauthorized() {
        ResponseEntity<ProviderCompanyDto> response = this.httpRequestBuilder
                .get(URL + "/companies").exchange(ProviderCompanyDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
