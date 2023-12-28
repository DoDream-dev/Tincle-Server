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
            String friendshipRelation
    ) {
        @Builder
        public OthersAccountInfoResponse {}
        public static OthersAccountInfoResponse of(MyAccountInfoResponse myAccountInfoResponse) {
            return OthersAccountInfoResponse.builder()
                    .accountId(myAccountInfoResponse.accountId)
                    .nickname(myAccountInfoResponse.nickname)
                    .status(myAccountInfoResponse.status)
                    .friendshipRelation("me").build();
        }
    }

    public record UpdateNicknameResponse(String nickname) {}

    public record UpdateStatusResponse(String status) {}

    public record CheckCodeResponse(boolean isDuplicated) {}


}
