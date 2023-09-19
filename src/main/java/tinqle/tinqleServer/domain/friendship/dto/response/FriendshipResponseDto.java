package tinqle.tinqleServer.domain.friendship.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

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
            String friendNickname,
            String statusImageUrl
    ) {
        @Builder
        public FriendshipCardResponse{}

        public static FriendshipCardResponse of(Friendship friendship) {
            if (friendship.isChangeFriendNickname()) {
                return FriendshipCardResponse.builder()
                        .accountId(friendship.getAccountFriend().getId())
                        .friendshipId(friendship.getId())
                        .friendNickname(friendship.getFriendNickname())
                        .statusImageUrl(friendship.getAccountFriend().getStatus().getStatusImageUrl())
                        .build();
            } else {
                return FriendshipCardResponse.builder()
                        .accountId(friendship.getAccountFriend().getId())
                        .friendshipId(friendship.getId())
                        .friendNickname(friendship.getAccountFriend().getNickname())
                        .statusImageUrl(friendship.getAccountFriend().getStatus().getStatusImageUrl())
                        .build();
            }
        }
    }
}
