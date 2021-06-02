package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedTrack {
    String name;
    String id;
    Integer duration_ms;
    Album album;
    Integer order;

    public OrderedTrack(Track track, Integer order) {
        this.name = track.getName();
        this.id = track.getId();
        this.duration_ms = track.getDuration_ms();
        this.album = track.getAlbum();
        this.order = order;
    }
}
