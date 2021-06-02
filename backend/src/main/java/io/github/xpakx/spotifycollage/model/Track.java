package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track {
    String name;
    String id;
    Integer duration_ms;
    Album album;
}