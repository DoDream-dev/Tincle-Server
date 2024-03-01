package tinqle.tinqleServer.domain.friendship.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveStatusElapsedTime;

public class FriendshipResponseDto {

    public record CodeResponse(
            String code
    ) {}

    public record ResponseFriendship(
            Long friendshipRequestId
    ) {}

    public record FriendshipReqeustResponse(
            boolean result
    ) {
        public static FriendshipReqeustResponse of(boolean result) {
            return new FriendshipReqeustResponse(!result);
        }
    }

    public record FriendshipCardResponse(
            Long accountId,
            Long friendshipId,
            String profileImageUrl,
            String friendNickname,
            String status,
            String lastChangeStatusAt
    ) {
        @Builder
        public FriendshipCardResponse{}

        public static FriendshipCardResponse of(Friendship friendship) {
            Account friend = friendship.getAccountFriend();
            return FriendshipCardResponse.builder()
                    .accountId(friend.getId())
                    .profileImageUrl(friend.getProfileImageUrl())
                    .friendshipId(friendship.getId())
                    .friendNickname((friendship.isChangeFriendNickname() ? friendship.getFriendNickname() : friend.getNickname()))
                    .status(friend.getStatus().toString())
                    .lastChangeStatusAt(friend.getLastChangeStatusAt() == null
                            ? resolveStatusElapsedTime(friend.getLastLoginAt())
                            : resolveStatusElapsedTime(friend.getLastChangeStatusAt()))
                    .build();
        }
    }

    public record ChangeFriendNicknameResponse(String friendNickname) {}

    public record FriendshipRequestMessageResponse(String message) {}

    public record DeleteFriendResponse(boolean result) {}
}
