package es.upm.miw.functionaltests;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class HttpRequestBuilder {

    private final String apiClientId;
    private final String apiClientSecret;
    private final String tokenUri;

    private final TestRestTemplate testRestTemplate;
    private final Map<String, Object> queryParams = new LinkedHashMap<>();
    private HttpMethod method;
    private String url;
    private Object[] uriVars;
    private String scope;
    private Object body;

    public HttpRequestBuilder(@Value("${spring.security.oauth2.api-client-id}") String apiClientId,
                              @Value("${spring.security.oauth2.api-client-secret}") String apiClientSecret,
                              @Value("${spring.security.oauth2.token-uri}") String tokenUri
    ) {
        this.apiClientId = apiClientId;
        this.apiClientSecret = apiClientSecret;
        this.tokenUri = tokenUri;
        this.testRestTemplate = new TestRestTemplate();
    }

    private HttpRequestBuilder httpMethod(HttpMethod method, String url, Object[] uriVars) {
        this.method = method;
        this.url = url;
        this.uriVars = uriVars;
        return this;
    }

    public HttpRequestBuilder get(String url, Object... uriVars) {
        return this.httpMethod(HttpMethod.GET, url, uriVars);
    }

    public HttpRequestBuilder param(String key, Object value) {
        if (key == null || key.isEmpty() || value == null) {
            throw new IllegalArgumentException("Query parameter key or value cannot be null or empty");
        }
        this.queryParams.put(key, value);
        return this;
    }

    public HttpRequestBuilder post(String url, Object... uriVars) {
        return this.httpMethod(HttpMethod.POST, url, uriVars);
    }

    public HttpRequestBuilder put(String url, Object... uriVars) {
        return this.httpMethod(HttpMethod.PUT, url, uriVars);
    }

    public HttpRequestBuilder delete(String url, Object... uriVars) {
        return this.httpMethod(HttpMethod.DELETE, url, uriVars);
    }

    public HttpRequestBuilder patch(String url, Object... uriVars) {
        return this.httpMethod(HttpMethod.PATCH, url, uriVars);
    }

    public HttpRequestBuilder scope(Scope scope) {
        this.scope = scope.value();
        return this;
    }

    public HttpRequestBuilder body(Object body) {
        this.body = body;
        return this;
    }

    private String obtainAccessToken(String scope) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String auth = apiClientId + ":" + apiClientSecret;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth);

        MultiValueMap<String, String> credentialsBody = new LinkedMultiValueMap<>();
        credentialsBody.add("grant_type", "client_credentials");
        credentialsBody.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(credentialsBody, headers);

        Map<?, ?> responseBody = Objects.requireNonNull(
                new RestTemplate().postForEntity(this.tokenUri, request, Map.class).getBody()
        );
        return responseBody.get("access_token").toString();
    }

    private HttpHeaders buildHeaders(String scope) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (scope != null && !scope.isEmpty()) {
            String token = obtainAccessToken(scope);
            headers.setBearerAuth(token);
        }
        return headers;
    }

    private HttpEntity<?> buildHttpEntity() {
        HttpHeaders headers = buildHeaders(this.scope);
        if (this.body != null) {
            return new HttpEntity<>(this.body, headers);
        } else {
            return new HttpEntity<>(headers);
        }
    }

    private String buildFinalUrl() {
        if (queryParams.isEmpty()) {
            return this.url;
        }
        StringBuilder finalUrl = new StringBuilder(this.url);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            finalUrl
                    .append("&")
                    .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8));
        }
        return finalUrl.toString().replaceFirst("&", "?");
    }

    public <R> ResponseEntity<R> exchange(Class<R> responseType) {
        if (this.method == null) {
            throw new IllegalArgumentException("HTTP method is required.");
        }
        if (this.url == null || this.url.isEmpty()) {
            throw new IllegalArgumentException("URL is required.");
        }
        HttpEntity<?> entity = buildHttpEntity();
        ResponseEntity<R> response = testRestTemplate.exchange(
                this.buildFinalUrl(),
                this.method,
                entity,
                responseType,
                this.uriVars != null ? this.uriVars : new Object[]{}
        );
        this.method = null;
        this.url = null;
        this.uriVars = null;
        this.scope = null;
        this.body = null;

        return response;
    }

}

