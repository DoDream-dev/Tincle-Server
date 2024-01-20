package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tinqle.tinqleServer.domain.account.dto.KakaoDto.RevokeKakaoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.OAuthSocialEmailAndNicknameAndRefreshTokenResponse;
import tinqle.tinqleServer.domain.account.model.SocialType;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final RestTemplate restTemplate;
    private final SocialService socialService;
    @Value("${kakao.admin-key}")
    private String adminKey;

    protected OAuthSocialEmailAndNicknameAndRefreshTokenResponse getKakaoId(String oauthAccessToken) {
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
        return OAuthSocialEmailAndNicknameAndRefreshTokenResponse.to(id + "@KAKAO", nickname, "kakao");
    }

    protected boolean revokeKakao(String socialUuid) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("target_id_type", "user_id");
        params.add("target_id", socialUuid);
        log.info("socialUuid = {}", socialUuid);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<RevokeKakaoResponse> response = restTemplate.postForEntity(unlinkUrl, httpEntity, RevokeKakaoResponse.class);
        int revokeStatusCode = response.getStatusCode().value();
        return revokeStatusCode == 200;
    }
}
