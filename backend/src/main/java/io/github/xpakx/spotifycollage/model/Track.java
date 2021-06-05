package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Track {
    String name;
    String id;
    Integer duration_ms;
    Album album;
    List<Artist> artists;
}
