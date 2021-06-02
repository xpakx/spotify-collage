package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpotifyPage<T> {
    List<T> items;
    Integer total;
    Integer limit;
    Integer offset;
    String href;
    String previous;
    String next;
}
