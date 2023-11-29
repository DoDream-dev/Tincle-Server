package tinqle.tinqleServer.domain.account.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tinqle.tinqleServer.domain.account.dto.GoogleDto.GoogleRefreshTokenResponse;
import tinqle.tinqleServer.domain.account.dto.GoogleDto.GoogleTokenResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.OAuthSocialEmailAndNicknameResponse;
import tinqle.tinqleServer.domain.account.model.SocialType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final SocialService socialService;

    private List<String> getGoogleIdAndName(String accessToken) {
        String socialUrl = SocialType.GOOGLE.getSocialUrl();
        HttpMethod httpMethod = SocialType.GOOGLE.getHttpMethod();
        ResponseEntity<Map<String, Object>> response = socialService.validOauthAccessToken(accessToken, socialUrl, httpMethod);
        String sub = (String) Objects.requireNonNull(response.getBody()).get("sub");
        String name = (String) Objects.requireNonNull(response.getBody()).get("name");

        return Arrays.asList(sub + "@GOOGLE", name);
    }

    public OAuthSocialEmailAndNicknameResponse requestGoogleToken(final String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = generateTokenParams(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, GoogleTokenResponse.class);
        GoogleTokenResponse responseBody = response.getBody();
        List<String> list = getGoogleIdAndName(responseBody.accessToken());
        log.info("Id={}", list.get(0));
        log.info("name={}", list.get(1));

        return OAuthSocialEmailAndNicknameResponse.to(list.get(0), list.get(1), responseBody.refreshToken());
    }

    private MultiValueMap<String, String> generateTokenParams(final String authorizationCode) {
        String code = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);
        return params;
    }

    protected boolean revokeGoogle(String refreshToken) {
        String googleAccessToken = getGoogleAccessToken(refreshToken);
        int revokeStatusCode = revoke(googleAccessToken);
        return revokeStatusCode == 200;
    }

    @NotNull
    private @JsonProperty("access_token") String getGoogleAccessToken(String refreshToken) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleRefreshTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, GoogleRefreshTokenResponse.class);
        return response.getBody().accessToken();
    }

    private int revoke(String accessToken) {
        String revokeUrl = "https://oauth2.googleapis.com/revoke";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("token", accessToken);
        ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl, parameters, String.class);
        return response.getStatusCode().value();
    }
}
