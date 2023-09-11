package tinqle.tinqleServer.domain.account.dto.response;

public class AccountResponseDto {

    public record AccountInfoResponse(
            String nickname,
            String statusImageUrl
    ) {}
}
