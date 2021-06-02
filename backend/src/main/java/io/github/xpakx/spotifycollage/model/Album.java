package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Album {
    String id;
    String name;
    List<Artist> artists;
    List<Cover> images;

    public Album getThis() {
        return this;
    }

}
