package tinqle.tinqleServer.domain.account.dto.response;

import lombok.Builder;

public class AccountResponseDto {

    public record MyAccountInfoResponse(
            Long accountId,
            String nickname,
            String status
    ) {}

    public record OthersAccountInfoResponse(
            Long accountId,
            String nickname,
            String status,
            Boolean isFriend
    ) {
        @Builder
        public OthersAccountInfoResponse {}
        public static OthersAccountInfoResponse of(MyAccountInfoResponse myAccountInfoResponse) {
            return OthersAccountInfoResponse.builder()
                    .accountId(myAccountInfoResponse.accountId)
                    .nickname(myAccountInfoResponse.nickname)
                    .status(myAccountInfoResponse.status)
                    .isFriend(null).build();
        }
    }

    public record UpdateNicknameResponse(String nickname) {}

    public record UpdateStatusResponse(String status) {}


}
