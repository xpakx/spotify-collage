package io.github.xpakx.spotifycollage.controller;

import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import io.github.xpakx.spotifycollage.model.CollageResponse;
import io.github.xpakx.spotifycollage.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";

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
    @ResponseBody
    public CollageResponse getSpotifyUserCode(@RequestParam("code") String userCode) throws IOException {
       String token = spotifyAuthService.requestTokens(userCode);
       return spotifyAuthService.getCollage(token);
    }

    //size: 3x3, 4x4, 5x5
    //term: long_term, medium_term, short_term
    //display caption?
}
