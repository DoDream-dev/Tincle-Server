package tinqle.tinqleServer.domain.account.dto.response;

public class AccountResponseDto {

    public record MyAccountInfoResponse(
            String nickname,
            String statusImageUrl
    ) {}

    public record OthersAccountInfoResponse(
            Long accountId,
            String nickname,
            String statusImageUrl,
            Boolean isFriend
    ) {}

    public record UpdateNicknameResponse(String nickname) {}

    public record UpdateStatusResponse(String statusImageUrl) {}


}
