package io.github.xpakx.spotifycollage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    String access_token;
    String token_type;
    String scope;
    Integer expires_in;
    String refresh_token;
}
