package tinqle.tinqleServer.domain.account.dto.request;

public class AccountRequestDto {

    public record UpdateNicknameRequest(String nickname) {}
    public record UpdateProfileImageUrlRequest(String profileImageUrl) {}
    public record UpdateFcmTokenRequest(String fcmToken) {}
}
