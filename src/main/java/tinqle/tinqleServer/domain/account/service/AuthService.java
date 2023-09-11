package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.config.jwt.JwtDto;
import tinqle.tinqleServer.config.jwt.JwtProvider;
import tinqle.tinqleServer.config.redis.RedisService;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.AccountDto.SigningAccount;
import tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.SocialLoginRequest;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.LoginMessageResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.SignTokenResponse;
import tinqle.tinqleServer.domain.account.exception.AuthException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.AccountPolicy;
import tinqle.tinqleServer.domain.account.repository.AccountPolicyRepository;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.policy.repository.PolicyRepository;
import tinqle.tinqleServer.util.UuidGenerateUtil;


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
    private final RedisService redisService;
    private final PolicyRepository policyRepository;
    private final AccountPolicyRepository accountPolicyRepository;

    @Transactional(noRollbackFor = {AuthException.class})
    public LoginMessageResponse loginAccess(SocialLoginRequest socialLoginRequest) {
        OAuthSocialEmailAndNicknameResponse response = fetchSocialEmail(socialLoginRequest);
        String socialEmail = response.socialEmail();
        String nickname = response.nickname().isBlank() ? "사람" : response.nickname();

        Optional<Account> findAccount = accountRepository.findBySocialEmail(socialEmail);
        LoginMessageResponse loginMessage;
        if(findAccount.isPresent()) {
            Account account = findAccount.get();
            account.updateLastLoginAt();
            account.updateFcmToken(socialLoginRequest.fcmToken());

            JwtDto jwtDto = login(LoginRequest.toLoginRequest(account));
            loginMessage = new LoginMessageResponse(
                    jwtDto,
                    StatusCode.LOGIN.getMessage()
            );
        } else {
            String signToken = jwtProvider.createSignToken(socialEmail, nickname);
            SignTokenResponse signTokenResponse = new SignTokenResponse(signToken);
            log.info("signTokenResponse={}", signTokenResponse.signToken());
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

    @Transactional
    public SignMessageResponse signUp(SignUpRequest signUpRequest) {
        String signToken = signUpRequest.signToken();
        if (!jwtProvider.validate(signToken))
            throw new AuthException(StatusCode.SIGNUP_TOKEN_ERROR);

        SigningAccount signKey = jwtProvider.getSignKey(signToken);
        String socialEmail = signKey.socialEmail();
        String socialType = signKey.socialType();
        String nickname = signKey.nickname();

        boolean exists = accountRepository.existsBySocialEmail(socialEmail);
        if (exists) throw new AuthException(StatusCode.ALREADY_EXIST_ACCOUNT);

        ConcurrentHashMap<String, Boolean> policyCheckList = new ConcurrentHashMap<>();
        policyCheckList.put("age", signUpRequest.agePolicy());
        policyCheckList.put("personal", signUpRequest.personalPolicy());
        policyCheckList.put("use", signUpRequest.usePolicy());
        policyCheckList.put("marketing", signUpRequest.marketPolicy());

        String code = iaMakeAndCheckDuplicateCode();
        if (code == null) throw new AuthException(StatusCode.CODE_CREATE_ERROR);

        Account account = signUpRequest.toAccount(socialEmail, socialType,nickname, code, passwordEncoder);
        accountRepository.save(account);
        account.updateFcmToken(signUpRequest.fcmToken());

        policyCheckList.forEach((name, isChecked) -> policyRepository.findByName(name).ifPresent(policy -> {
            AccountPolicy accountPolicy = AccountPolicy.builder()
                    .account(account)
                    .isChecked(isChecked)
                    .policy(policy)
                    .build();
            accountPolicyRepository.save(accountPolicy);
            account.addAccountPolicy(accountPolicy);
        }));

        JwtDto jwtDto = login(LoginRequest.toLoginRequest(account));
        return new SignMessageResponse(
                jwtDto,
                StatusCode.SIGNUP_COMPLETE.getMessage()
        );
    }

    private String iaMakeAndCheckDuplicateCode() {
        for (int cnt = 0; cnt < 5 ; cnt++) {
            String code = UuidGenerateUtil.makeRandomUuid();

            boolean exists = accountRepository.existsByCode(code);
            if (!exists) return code;
        }
        return null;
    }

    private OAuthSocialEmailAndNicknameResponse fetchSocialEmail(SocialLoginRequest socialLoginRequest) {
        String provider = socialLoginRequest.socialType();
        if (provider.equalsIgnoreCase("Google")) {
            return googleService.requestGoogleToken(socialLoginRequest.authorizationCode());
        } else if (provider.equalsIgnoreCase("Kakao")) {
            return kakaoService.getKakaoId(socialLoginRequest.oauthAccessToken());
        } else {
            throw  new AuthException(StatusCode.SOCIAL_TYPE_ERROR);
        }
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
