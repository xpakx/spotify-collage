package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollageRequest {
    String term;
    Integer size;
    String token;
    boolean captions;
}

//size: 3x3, 4x4, 5x5
//term: long_term, medium_term, short_term