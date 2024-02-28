package tinqle.tinqleServer.domain.room.dto.request;

public class RoomRequestDto {

    public record CreateRoomRequest(
            Long targetAccountId
    ) {}
}
