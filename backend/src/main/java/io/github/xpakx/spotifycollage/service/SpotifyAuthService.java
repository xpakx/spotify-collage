package io.github.xpakx.spotifycollage.service;

import io.github.xpakx.spotifycollage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SpotifyAuthService {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";
    private final String redir = "http://192.168.1.204:4200/redirect";
    private final String scope = "user-read-private, user-top-read";
    private final String state = "i4R8utEkEBy946";

    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(SpotifyAuthService.class);

    public SpotifyAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthAddressResponse getAuthorizationUri() {
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

    public List<Track> getBestTracks(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<SpotifyPage> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?limit=50",
                HttpMethod.GET,
                entity,
                SpotifyPage.class
        );

        SpotifyPage<Track> responseBody = (SpotifyPage<Track>) ((SpotifyPage<?>) response.getBody());
        List<Track> tracks = responseBody.getItems();
        return tracks;
    }

    private List<Album> generateAlbumList(List<Track> tracks, Integer size) {
        Map<String, Album> albums = tracks.stream()
                .map(Track::getAlbum)
                .collect(Collectors.toMap(
                        Album::getId,
                        Album::getThis,
                        (existing, replacement) -> existing
                ));

        List<Album> result = IntStream.range(0,tracks.size())
                .mapToObj((i) -> new OrderedTrack(tracks.get(i), tracks.size()-i))
                .collect(
                        Collectors.groupingBy(
                                (t) -> t.getAlbum().getId(),
                                Collectors.summingInt(OrderedTrack::getOrder)
                        )
                ).entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(size*size)
                .map(Map.Entry::getKey)
                .map(albums::get)
                .collect(Collectors.toList());

        return result;
    }


    public CollageResponse getCollage(CollageRequest request) {
		String token = requestTokens(request.getToken());
		
        CollageResponse response = new CollageResponse();
        response.setUsername(getUserData(token));
        response.setAlbums(
                generateAlbumList(
                        getBestTracks(token),
                        request.getSize()
                )
        );
        return response;
    }
}
