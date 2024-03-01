package tinqle.tinqleServer.domain.account.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveStatusElapsedTime;

public class AccountResponseDto {

    public record MyAccountInfoResponse(
            Long accountId,
            String nickname,
            String status,
            String profileImageUrl,
            String lastChangeStatusAt
    ) {
        public static MyAccountInfoResponse of(Account account) {
            return new MyAccountInfoResponse(
                    account.getId(),
                    account.getNickname(),
                    account.getStatus().toString(),
                    account.getProfileImageUrl(),
                    (account.getLastChangeStatusAt() == null
                            ? resolveStatusElapsedTime(account.getLastLoginAt())
                            : resolveStatusElapsedTime(account.getLastChangeStatusAt())
                    ));
        }
    }

    public record OthersAccountInfoResponse(
            Long accountId,
            String nickname,
            String status,
            String friendshipRelation,
            Long friendshipId,
            Long friendshipRequestId,
            String profileImageUrl,
            String lastChangeStatusAt
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
                    .lastChangeStatusAt(myAccountInfoResponse.lastChangeStatusAt)
                    .build();
        }

        public static OthersAccountInfoResponse of(Friendship friendship) {
            Account account = friendship.getAccountFriend();
            return OthersAccountInfoResponse.builder()
                    .accountId(account.getId())
                    .nickname((friendship.isChangeFriendNickname() ? friendship.getFriendNickname() : account.getNickname()))
                    .status(account.getStatus().toString())
                    .friendshipRelation("true")
                    .friendshipId(friendship.getId())
                    .friendshipRequestId(0L)
                    .profileImageUrl(account.getProfileImageUrl())
                    .lastChangeStatusAt(account.getLastChangeStatusAt() == null
                        ? resolveStatusElapsedTime(account.getLastLoginAt())
                        : resolveStatusElapsedTime(account.getLastChangeStatusAt()))
                    .build();
        }

        public static OthersAccountInfoResponse of(Account account, String friendshipRelation, Long friendshipRequestId) {
            return OthersAccountInfoResponse.builder()
                    .accountId(account.getId())
                    .nickname(account.getNickname())
                    .status(account.getStatus().toString())
                    .friendshipRelation(friendshipRelation)
                    .friendshipId(0L)
                    .friendshipRequestId(friendshipRequestId)
                    .profileImageUrl(account.getProfileImageUrl())
                    .lastChangeStatusAt(account.getLastChangeStatusAt() == null
                            ? resolveStatusElapsedTime(account.getLastLoginAt())
                            : resolveStatusElapsedTime(account.getLastChangeStatusAt()))
                    .build();
        }
    }

    public record UpdateNicknameResponse(String nickname) {}

    public record UpdateStatusResponse(String status) {}

    public record UpdateProfileImageUrlResponse(String profileImageUrl) {}

    public record UpdateCodeResponse(String code) {}

    public record UpdateFcmTokenResponse(String fcmToken) {}

    public record CheckCodeResponse(boolean isDuplicated) {}

    public record PushNotificationStatusResponse(boolean isReceivedPushNotification) { }

    public record SearchCodeResponse(
            OthersAccountInfoResponse equalKeywordAccount,
            SliceResponse<OthersAccountInfoResponse> containKeywordAccounts) {}

}
