package tinqle.tinqleServer.domain.friendship.dto.response;

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

}
