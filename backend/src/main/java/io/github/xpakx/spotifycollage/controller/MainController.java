package io.github.xpakx.spotifycollage.controller;

import io.github.xpakx.spotifycollage.model.*;
import io.github.xpakx.spotifycollage.service.SpotifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;

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

    @PostMapping("playlists")
    @ResponseBody
    public SpotifyPage<Playlist> getPlaylists(@RequestBody @Valid Token request) {
        return spotifyAuthService.getPlaylists(request);
    }

    @PostMapping("playlists/{id}")
    @ResponseBody
    public SpotifyPage<TrackWrapper> getPlaylistTracks(@RequestBody @Valid Token request, @PathVariable("id") String id) {
        return spotifyAuthService.getPlaylistTracks(request, id);
    }

    @PostMapping("top")
    @ResponseBody
    public SpotifyPage<Track> getTopTracks(@RequestBody @Valid Token request) {
        return spotifyAuthService.getBestTracks(request);
    }

    @GetMapping(value = "playlists/{id}/image", produces = "image/jpg")
    @ResponseBody
    public byte[] getPlaylistCollage(@PathVariable String id, @RequestParam String token)  {
        return spotifyAuthService.getPlaylistCollage(token, id);
    }

    @GetMapping(value = "top/image", produces = "image/jpg")
    @ResponseBody
    public byte[] getBestTracksCollage(@RequestParam String token)  {
        return spotifyAuthService.getBestTracksCollage(token);
    }
}
