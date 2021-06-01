package io.github.xpakx.spotifycollage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetTokenRequest {
    String client_id;
    String client_secret;
    String grant_type;
    String code;
    String redirect_uri;
}
