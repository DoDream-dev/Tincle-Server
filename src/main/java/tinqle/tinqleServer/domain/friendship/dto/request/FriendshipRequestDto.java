package tinqle.tinqleServer.domain.friendship.dto.request;

import jakarta.validation.constraints.*;

import static tinqle.tinqleServer.common.constant.ValidConstants.FRIENDSHIP_REQUEST_MESSAGE_LENGTH;

public class FriendshipRequestDto {
    public record RequestFriendship(
            @Positive
            Long accountTargetId,
            @Size(max = FRIENDSHIP_REQUEST_MESSAGE_LENGTH)
            String message
    ) {}

    public record ChangeFriendNicknameRequest(
            @Positive
            Long friendAccountId,
            @NotNull
            String nickname
    ) {}
}
