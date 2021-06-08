package io.github.xpakx.spotifycollage.service;

import io.github.xpakx.spotifycollage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SpotifyService {
    @Value("${spotify.client-id}")
    private String clientId = "";
    @Value("${spotify.client-secret}")
    private String clientSecret = "";
    private final String redir = "http://192.168.1.204:4200/redirect";
    private final String scope = "user-read-private user-top-read playlist-read-private";
    private final String state = "i4R8utEkEBy946";

    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(SpotifyService.class);

    public SpotifyService(RestTemplate restTemplate) {
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

    public TokenForClient requestToken(RequestWithCode requestFromClient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        GetTokenRequest tokenRequest = GetTokenRequest.builder()
                .client_id((clientId))
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(requestFromClient.getCode())
                .redirect_uri(redir)
                .build();
        HttpEntity<String> request = new HttpEntity<>(tokenRequest.toString(), headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                TokenResponse.class
        );

        if(response.getStatusCode().isError()) {
            return null;
        }
        else {
            logger.debug("Token: " + response.getBody().getAccess_token());
            TokenForClient result = new TokenForClient();
            result.setToken(response.getBody().getAccess_token());
            result.setUsername(getUserData(result.getToken()));
            return result;
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

    public SpotifyPage<Track> getBestTracks(Token token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<SpotifyPage<Track>> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?limit=50",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<SpotifyPage<Track>>() {}
        );

        SpotifyPage<Track> responseBody = response.getBody();
        return responseBody;
    }

    public SpotifyPage<Playlist> getPlaylists(Token token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<SpotifyPage> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/playlists?limit=50",
                HttpMethod.GET,
                entity,
                SpotifyPage.class
        );

        SpotifyPage<Playlist> result = (SpotifyPage<Playlist>) ((SpotifyPage<?>) response.getBody());

        return result;
    }

    public SpotifyPage<TrackWrapper> getPlaylistTracks(Token token, String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<PlaylistWithTracks> response = restTemplate.exchange(
                "https://api.spotify.com/v1/playlists/"+id,
                HttpMethod.GET,
                entity,
                PlaylistWithTracks.class
        );

        return response.getBody().getTracks();
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

    public CollageResponse getCollageToBytes(CollageRequest request) {
        CollageResponse response = new CollageResponse();
        response.setUsername(getUserData(request.getToken()));

        return response;
    }

    private Image getImageFromUri(String uri) {
        Image image = null;
        try {
            URL url = new URL(uri);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public byte[] getPlaylistCollage(String token, String id) {
        Integer size = 3;
        Token temp = new Token(); temp.setToken(token);
        List<Track> tracks = getPlaylistTracks(temp, id)
                .getItems()
                .stream()
                .map(TrackWrapper::getTrack)
                .collect(Collectors.toList());
        List<Image> albums = generateAlbumList(tracks, size)
                .stream()
                .map((a) -> getImageFromUri(a.getImages().get(0).getUrl()))
                .collect(Collectors.toList());
        return getCollageToBytes(size, albums);
    }

    public byte[] getBestTracksCollage(String token) {
        Integer size = 3;
        Token temp = new Token(); temp.setToken(token);
        List<Track> tracks = getBestTracks(temp)
                .getItems();
        List<Image> albums = generateAlbumList(tracks, size)
                .stream()
                .map((a) -> getImageFromUri(a.getImages().get(0).getUrl()))
                .collect(Collectors.toList());
        return getCollageToBytes(size, albums);
    }

    private byte[] getCollageToBytes(Integer size, List<Image> albums) {
        BufferedImage image =
                new BufferedImage(size *640, size *640,
                        BufferedImage.TYPE_INT_RGB);

        Graphics g = image.createGraphics();

        int i = 0;
        for(Image album : albums) {
            g.drawImage(album,(i% size)*640,(i/ size)*640, null);
            i++;
        }
        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", bao);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bao.toByteArray();
    }
}
