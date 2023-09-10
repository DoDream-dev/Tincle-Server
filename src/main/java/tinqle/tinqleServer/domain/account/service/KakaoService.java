package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.OAuthSocialEmailAndNicknameResponse;
import tinqle.tinqleServer.domain.account.model.SocialType;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final RestTemplate restTemplate;
    private final SocialService socialService;

    protected OAuthSocialEmailAndNicknameResponse getKakaoId(String oauthAccessToken) {
        String socialUrl = SocialType.KAKAO.getSocialUrl();
        HttpMethod httpMethod = SocialType.KAKAO.getHttpMethod();
        ResponseEntity<Map<String, Object>> response =
                socialService.validOauthAccessToken(oauthAccessToken, socialUrl, httpMethod);
        Map<String, Object> oauthBody = response.getBody();
        String id = String.valueOf(oauthBody.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) oauthBody.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = String.valueOf(profile.get("nickname"));

        log.info("id = {}", id);
        log.info("nickname = {}", nickname);
        return OAuthSocialEmailAndNicknameResponse.to(id + "@KAKAO", nickname);
    }
}
