package tinqle.tinqleServer.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.*;

import java.time.LocalDateTime;

public class AuthRequestDto {

    public record SocialLoginRequest(
            @NotBlank
            String socialType,
            String oauthAccessToken,
            String authorizationCode,
            String fcmToken
    ){}

    public record SignUpRequest(
            @NotBlank
            String signToken,
            boolean usePolicy,
            boolean agePolicy,
            boolean personalPolicy,
//            boolean marketPolicy,
            String fcmToken,
            @NotBlank
            String code
    ) {
        @Builder
        public SignUpRequest {
        }

        public SocialType getSocialType(String socialType) {
            if (socialType.equalsIgnoreCase(SocialType.KAKAO.name())) {
                return SocialType.KAKAO;
            } else if (socialType.equalsIgnoreCase(SocialType.GOOGLE.name())) {
                return SocialType.GOOGLE;
            } else throw new AccountException(StatusCode.SOCIAL_TYPE_ERROR);
        }

        public Account toAccount(String socialEmail, String socialType, String nickname, String code, PasswordEncoder passwordEncoder) {
            return Account.builder()
                    .password(passwordEncoder.encode(socialEmail + "tinqle"))
                    .socialType(getSocialType(socialType))
                    .socialEmail(socialEmail)
                    .nickname(nickname)
                    .accountStatus(AccountStatus.NORMAL)
                    .code(code)
                    .role(Role.ROLE_USER)
                    .lastLoginAt(LocalDateTime.now())
                    .status(Status.SMILE)
                    .isReceivedPushNotification(true)
                    .profileImageUrl(null)
                    .build();
        }
    }

    public record LoginRequest(String socialEmail, String password) {
        @Builder
        public LoginRequest {
        }

        public static LoginRequest toLoginRequest(Account account) {
            String socialEmail = account.getSocialEmail();
            return LoginRequest.builder()
                    .socialEmail(socialEmail)
                    .password(socialEmail + "tinqle")
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(socialEmail, password);
        }

    }

    public record ReissueRequest(
            @NotBlank
            String refreshToken) {}
}
