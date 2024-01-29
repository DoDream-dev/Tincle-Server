package tinqle.tinqleServer.domain.account.dto.response;

import lombok.Builder;

public class AccountResponseDto {

    public record MyAccountInfoResponse(
            Long accountId,
            String nickname,
            String status,
            String profileImageUrl
    ) {}

    public record OthersAccountInfoResponse(
            Long accountId,
            String nickname,
            String status,
            String friendshipRelation,
            Long friendshipId,
            Long friendshipRequestId,
            String profileImageUrl
    ) {
        @Builder
        public OthersAccountInfoResponse {}
        public static OthersAccountInfoResponse of(MyAccountInfoResponse myAccountInfoResponse) {
            return OthersAccountInfoResponse.builder()
                    .accountId(myAccountInfoResponse.accountId)
                    .nickname(myAccountInfoResponse.nickname)
                    .status(myAccountInfoResponse.status)
                    .friendshipRelation("me")
                    .friendshipId(0L)
                    .friendshipRequestId(0L)
                    .profileImageUrl(myAccountInfoResponse.profileImageUrl)
                    .build();
        }
    }

    public record UpdateNicknameResponse(String nickname) {}

    public record UpdateStatusResponse(String status) {}

    public record UpdateProfileImageUrlResponse(String profileImageUrl) {}

    public record UpdateFcmTokenResponse(String fcmToken) {}

    public record CheckCodeResponse(boolean isDuplicated) {}

    public record PushNotificationStatusResponse(boolean isReceivedPushNotification) { }

}
