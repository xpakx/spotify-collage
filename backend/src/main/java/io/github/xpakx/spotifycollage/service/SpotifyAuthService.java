package io.github.xpakx.spotifycollage.service;

import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SpotifyAuthService {
    @Value("${spotify.client-id}")
    private String clientId = "";
    private final String redir = "http://192.168.1.204:8080/redirect";
    private final String scope = "user-read-private, user-top-read";
    private final String state = "i4R8utEkEBy946";

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
}
