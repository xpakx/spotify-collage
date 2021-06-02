package io.github.xpakx.spotifycollage.service;

import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import io.github.xpakx.spotifycollage.model.GetTokenRequest;
import io.github.xpakx.spotifycollage.model.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Service
public class SpotifyAuthService {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";
    private final String redir = "http://192.168.1.204:8080/redirect";
    private final String scope = "user-read-private, user-top-read";
    private final String state = "i4R8utEkEBy946";

    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(SpotifyAuthService.class);

    public SpotifyAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
        HttpEntity<String> request = new HttpEntity<>(tokenRequest.toString(), headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                TokenResponse.class
        );

        if(response.getStatusCode().isError()) {
            return "error";
        }
        else {
            logger.debug("Token: " + response.getBody().getAccess_token());
            return response.getBody().getAccess_token();
        }
    }

    public String getUserData(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                entity,
                Object.class
        );
        LinkedHashMap result = (LinkedHashMap) response.getBody();

        return (String) result.get("display_name");
    }

    public String getBestTracks(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?limit=50",
                HttpMethod.GET,
                entity,
                Object.class
        );
        LinkedHashMap result = (LinkedHashMap) response.getBody();

        return result.toString();

    }

    /*
    album->id
    album->images->url, lowest height?
    album->images->name
    album->artists->id
    album->artists->name
    duration_ms
    id
    name
     */
}
