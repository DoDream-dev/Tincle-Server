package tinqle.tinqleServer.domain.account.dto.response;

import tinqle.tinqleServer.config.jwt.JwtDto;

public class AuthResponseDto {

    public record LoginMessageResponse(Object detailData, String detailMessage) {
    }

    public record SignTokenResponse(String signToken) {
    }

    public record SignMessageResponse(JwtDto detaildata, String detailMessage) {
    }

    public record CheckVersionResponse(boolean isAccessible) {}


    public record LogoutResponse(boolean isLogout) {
        public static LogoutResponse of(boolean isLogout) {
            return new LogoutResponse(!isLogout);
        }
    }

    public record OAuthSocialEmailAndNicknameAndRefreshTokenResponse(String socialEmail, String nickname, String refreshToken) {

        public static OAuthSocialEmailAndNicknameAndRefreshTokenResponse to(String socialEmail, String nickname, String refreshToken) {
            return new OAuthSocialEmailAndNicknameAndRefreshTokenResponse(socialEmail, nickname, refreshToken);
        }
    }

    public record RevokeResponse(Boolean revoke) {
    }

}
