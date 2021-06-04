package io.github.xpakx.spotifycollage.controller;

import io.github.xpakx.spotifycollage.model.*;
import io.github.xpakx.spotifycollage.service.SpotifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
public class MainController {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";

    private final SpotifyService spotifyAuthService;

    public MainController(SpotifyService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }


    @GetMapping("login")
    @ResponseBody
    public AuthAddressResponse spotifyLogin() throws Exception {
        return spotifyAuthService.getAuthorizationUri();
    }

    @PostMapping("token")
    @ResponseBody
    public TokenForClient getToken(@RequestBody RequestWithCode request) {
        return spotifyAuthService.requestToken(request);
    }

    @PostMapping("image")
    @ResponseBody
    public CollageResponse getCollage(@RequestBody @Valid CollageRequest request) {
        return spotifyAuthService.getCollage(request);
    }

    @PostMapping("playlists")
    @ResponseBody
    public SpotifyPage<Playlist> getPlaylists(@RequestBody @Valid Token request) {
        return spotifyAuthService.getPlaylists(request);
    }

    @GetMapping("playlists/{id}")
    @ResponseBody
    public SpotifyPage<Track> getPlaylistTracks(@RequestBody @Valid Token request, @PathParam("id") String id) {
        return spotifyAuthService.getPlaylistTracks(request, id);
    }
}
