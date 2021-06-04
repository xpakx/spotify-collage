package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistWithTracks {
    String id;
    String name;
    SpotifyPage<Track> tracks;
}
