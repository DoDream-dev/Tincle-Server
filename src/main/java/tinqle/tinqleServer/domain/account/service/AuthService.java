package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.model.Device;
import tinqle.tinqleServer.config.jwt.JwtDto;
import tinqle.tinqleServer.config.jwt.JwtProvider;
import tinqle.tinqleServer.config.redis.RedisService;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.AccountDto.SigningAccount;
import tinqle.tinqleServer.domain.account.exception.AuthException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.AccountPolicy;
import tinqle.tinqleServer.domain.account.repository.AccountPolicyRepository;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.policy.repository.PolicyRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.*;
import static tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final AppleService appleService;
    private final RedisService redisService;
    private final AccountService accountService;
    private final FeedService feedService;
    private final PolicyRepository policyRepository;
    private final AccountPolicyRepository accountPolicyRepository;

    @Transactional(noRollbackFor = {AuthException.class})
    public LoginMessageResponse loginAccess(SocialLoginRequest socialLoginRequest) {
        OAuthSocialEmailAndNicknameAndRefreshTokenResponse response = fetchSocialEmail(socialLoginRequest);
        String socialEmail = response.socialEmail();
        String nickname = response.nickname().isBlank() ? "팅클러" : response.nickname();
        String refreshToken = response.refreshToken();

        Optional<Account> findAccount = accountRepository.findBySocialEmail(socialEmail);
        LoginMessageResponse loginMessage;
        if (findAccount.isPresent()) {
            Account account = findAccount.get();
            updateFcmAndRefreshTokenAndLastLoginAt(account, socialLoginRequest.fcmToken(), refreshToken);

            JwtDto jwtDto = login(LoginRequest.toLoginRequest(account));
            loginMessage = new LoginMessageResponse(
                    jwtDto,
                    StatusCode.LOGIN.getMessage()
            );
        } else {
            String signToken = jwtProvider.createSignToken(socialEmail, nickname, refreshToken);
            SignTokenResponse signTokenResponse = new SignTokenResponse(signToken);
            throw new AuthException(StatusCode.NEED_TO_SIGNUP, signTokenResponse);
        }
        return loginMessage;
    }

    public JwtDto login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Account account = principal.getAccount();
        return jwtProvider.issue(account);
    }

    private void updateFcmAndRefreshTokenAndLastLoginAt(Account account, String fcmToken, String refreshToken) {
        account.updateLastLoginAt();
        account.updateFcmToken(fcmToken);
        account.updateProviderRefreshToken(refreshToken);
    }

    @Transactional
    public SignMessageResponse signUp(SignUpRequest signUpRequest) {
        String signToken = signUpRequest.signToken();
        if (!jwtProvider.validate(signToken))
            throw new AuthException(StatusCode.SIGNUP_TOKEN_ERROR);

        SigningAccount signKey = jwtProvider.getSignKey(signToken);
        String socialEmail = signKey.socialEmail();
        String socialType = signKey.socialType();
        String nickname = signKey.nickname();
        String refreshToken = signKey.refreshToken();
        if (nickname.length() > 10) {
            nickname = nickname.substring(0, 10);    //닉네임이 10자가 넘어갈 시 10자까지 짜르기
        }

        boolean exists = accountRepository.existsBySocialEmail(socialEmail);
        if (exists) throw new AuthException(StatusCode.ALREADY_EXIST_ACCOUNT);

        ConcurrentHashMap<String, Boolean> policyCheckList = putPolicy(signUpRequest);

        String code = signUpRequest.code();
        accountService.validateDuplicatedCode(code);

        Account account = signUpRequest.toAccount(socialEmail, socialType, nickname, code, passwordEncoder);
        accountRepository.save(account);
        updateFcmAndRefreshTokenAndLastLoginAt(account, signUpRequest.fcmToken(), refreshToken);

        policyCheckList.forEach((name, isChecked) -> policyRepository.findByName(name).ifPresent(policy -> {
            AccountPolicy accountPolicy = AccountPolicy.builder()
                    .account(account)
                    .isChecked(isChecked)
                    .policy(policy)
                    .build();
            accountPolicyRepository.save(accountPolicy);
            account.addAccountPolicy(accountPolicy);
        }));

        feedService.createWelcomeFeed(account.getId());

        JwtDto jwtDto = login(LoginRequest.toLoginRequest(account));
        return new SignMessageResponse(
                jwtDto,
                StatusCode.SIGNUP_COMPLETE.getMessage()
        );
    }

    @NotNull
    private static ConcurrentHashMap<String, Boolean> putPolicy(SignUpRequest signUpRequest) {
        ConcurrentHashMap<String, Boolean> policyCheckList = new ConcurrentHashMap<>();
        policyCheckList.put("age", signUpRequest.agePolicy());
        policyCheckList.put("personal", signUpRequest.personalPolicy());
        policyCheckList.put("use", signUpRequest.usePolicy());
//        policyCheckList.put("marketing", signUpRequest.marketPolicy());
        return policyCheckList;
    }

    private OAuthSocialEmailAndNicknameAndRefreshTokenResponse fetchSocialEmail(SocialLoginRequest socialLoginRequest) {
        String provider = socialLoginRequest.socialType();
        if (provider.equalsIgnoreCase("Google")) {
            return googleService.requestGoogleToken(socialLoginRequest.authorizationCode());
        } else if (provider.equalsIgnoreCase("Kakao")) {
            return kakaoService.getKakaoId(socialLoginRequest.oauthAccessToken());
        } else if (provider.equalsIgnoreCase("Apple")) {
            return appleService.requestAppleToken(socialLoginRequest);
        } else {
            throw new AuthException(StatusCode.SOCIAL_TYPE_ERROR);
        }
    }

    public CheckVersionResponse checkVersion(String deviceType, String version) {
        Device device = Device.toEntity(deviceType);
        boolean equals = device.getVersions().contains(version);

        return new CheckVersionResponse(equals);
    }

    @Transactional
    public JwtDto reissue(ReissueRequest reissueRequest) {
        return jwtProvider.reissue(reissueRequest.refreshToken());
    }

    @Transactional
    public LogoutResponse logout(Long accountId) {
        Account loginAccount = accountRepository.findById(accountId).orElseThrow(() -> new AuthException(StatusCode.NOT_FOUND_ACCOUNT));
        loginAccount.deleteFcmToken();
        redisService.deleteValues(loginAccount.getSocialEmail());
        String values = redisService.getValues(loginAccount.getSocialEmail());
        boolean exist = checkIsExistRefresh(values);

        return LogoutResponse.of(exist);
    }

    private boolean checkIsExistRefresh(String values) {
        return !Objects.isNull(values);
    }
}
