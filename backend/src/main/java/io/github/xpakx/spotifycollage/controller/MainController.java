package io.github.xpakx.spotifycollage.controller;

import io.github.xpakx.spotifycollage.model.AuthAddressResponse;
import io.github.xpakx.spotifycollage.model.CollageRequest;
import io.github.xpakx.spotifycollage.model.CollageResponse;
import io.github.xpakx.spotifycollage.service.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
       CollageRequest request = new CollageRequest();
       request.setToken(token);
       request.setSize(5);
       return spotifyAuthService.getCollage(request);
    }

    @PostMapping("image")
    @ResponseBody
    public CollageResponse getCollage(@RequestBody @Valid CollageRequest request) {
        return spotifyAuthService.getCollage(request);
    }
}
