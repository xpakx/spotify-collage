package io.github.xpakx.spotifycollage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@Builder
public class GetTokenRequest {
    String client_id;
    String client_secret;
    String grant_type;
    String code;
    String redirect_uri;

    @Override
    public String toString() {
        return "grant_type=" + URLEncoder.encode(grant_type, StandardCharsets.UTF_8)
                +"&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                +"&client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8)
                +"&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8)
                +"&redirect_uri=" + URLEncoder.encode(redirect_uri, StandardCharsets.UTF_8) ;
    }
}
