package tinqle.tinqleServer.domain.account.dto.request;

public class AccountRequestDto {

    public record ChangeNicknameRequest(String nickname) {}
    public record ChangeProfileImageUrlRequest(String profileImageUrl) {}
}
