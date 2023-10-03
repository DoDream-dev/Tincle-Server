package tinqle.tinqleServer.domain.friendship.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public class FriendshipRequestDto {
    public record RequestFriendship(
            @NotBlank
            Long accountTargetId,
            @Max(30)
            String message
    ) {}

    public record ChangeFriendNicknameRequest(
            Long friendAccountId,
            String nickname
    ) {}
}
