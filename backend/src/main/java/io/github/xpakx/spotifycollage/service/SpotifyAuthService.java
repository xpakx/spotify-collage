package io.github.xpakx.spotifycollage.service;

import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import io.github.xpakx.spotifycollage.model.GetTokenRequest;
import io.github.xpakx.spotifycollage.model.TokenResponse;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class SpotifyAuthService {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";
    private final String redir = "http://192.168.1.204:8080/redirect";
    private final String scope = "user-read-private, user-top-read";
    private final String state = "i4R8utEkEBy946";

    private RestTemplate restTemplate;

    public AuthAddressResponse getAuthorizationUri() throws UnsupportedEncodingException {
        String uri = "https://accounts.spotify.com/authorize?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(redir, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&state=" + state;
        AuthAddressResponse response = new AuthAddressResponse();
        response.setUrl(uri);
        return response;
    }

    public String requestTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        GetTokenRequest tokenRequest = GetTokenRequest.builder()
                .client_id((clientId))
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri(redir)
                .build();
        HttpEntity<GetTokenRequest> request = new HttpEntity<>(tokenRequest, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                TokenResponse.class
        );

        if(response.getStatusCode().isError()) {
            return response.getBody().getAccess_token();
        }
        else {
            return null;
        }
    }
}
