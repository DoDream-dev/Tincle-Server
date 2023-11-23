package tinqle.tinqleServer.domain.account.dto.response;

import tinqle.tinqleServer.config.jwt.JwtDto;

public class AuthResponseDto {

    public record LoginMessageResponse(Object detailData, String detailMessage) {}

    public record SignTokenResponse(String signToken) {}

    public record SignMessageResponse(JwtDto detaildata, String detailMessage) {}

    public record LogoutResponse(boolean isLogout) {
        public static LogoutResponse of(boolean isLogout) {
            return new LogoutResponse(!isLogout);
        }
    }

    public record OAuthSocialEmailAndNicknameResponse(String socialEmail, String nickname) {

        public static OAuthSocialEmailAndNicknameResponse to(String socialEmail, String nickname) {
            return new OAuthSocialEmailAndNicknameResponse(socialEmail, nickname);
        }
    }
}
