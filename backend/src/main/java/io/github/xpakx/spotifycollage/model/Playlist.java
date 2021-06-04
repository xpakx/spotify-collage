package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Playlist {
    String id;
    String name;
    TrackCount tracks;
}
