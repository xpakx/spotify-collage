package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackWrapper {
    Track track;
    boolean is_local;
    String added_at;
}
