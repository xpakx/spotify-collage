package io.github.xpakx.spotifycollage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public Album getThis() {
        return this;
    }

}
