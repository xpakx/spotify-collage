package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class CollageRequest {
    @Pattern(regexp = "^long_term|medium_term|short_term$")
    String term;
    @Min(3) @Max(5)
    Integer size;
    @NotNull
    String token;
    boolean captions;
}