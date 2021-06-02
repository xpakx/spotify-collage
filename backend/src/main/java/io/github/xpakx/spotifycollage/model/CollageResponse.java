package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CollageResponse {
    List<Track> tracks;
    String username;
}
