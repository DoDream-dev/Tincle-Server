package tinqle.tinqleServer.domain.account.dto.request;

public class AccountRequestDto {

    public record UpdateNicknameRequest(String nickname) {}
    public record UpdateProfileImageUrlRequest(String profileImageUrl) {}
    public record UpdateCodeRequest(String code) {}
    public record UpdateFcmTokenRequest(String fcmToken) {}
}
