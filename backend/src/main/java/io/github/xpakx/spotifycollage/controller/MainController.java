package io.github.xpakx.spotifycollage.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import io.github.xpakx.spotifycollage.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
public class MainController {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";

    private SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(SpotifyHttpManager.makeUri("http://192.168.1.204:8080/redirect"))
            .build();

    private final SpotifyAuthService spotifyAuthService;

    public MainController(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }


    @GetMapping("login")
    @ResponseBody
    public AuthAddressResponse spotifyLogin() throws Exception {
        return spotifyAuthService.getAuthorizationUri();
    }

    @GetMapping("redirect")
    public String getSpotifyUserCode(@RequestParam("code") String userCode) throws IOException {
       AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(userCode).build();
        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        } catch(IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {

        }
        return spotifyApi.getAccessToken();
    }
}
