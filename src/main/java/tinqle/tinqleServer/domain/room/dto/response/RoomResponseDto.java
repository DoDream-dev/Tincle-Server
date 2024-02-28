package tinqle.tinqleServer.domain.room.dto.response;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.room.model.Room;

import java.util.List;

public class RoomResponseDto {

    public record CreateRoomResponse(
            Long roomId,
            String createdAt
    ) {}

    public record RoomCardResponse(
            Long accountId,
            String profileImageUrl,
            String status,
            String nickname,
            String content,
            Long roomId,
            Long unreadCount,
            String messageCreatedAt
            ) {
        public static RoomCardResponse of(Account account, String nickname, Room room) {
            List<Message> messages = room.getMessages();
            Message lastMessage = messages.get(messages.size() - 1);
            long unreadCount = messages.stream().map(message -> !message.isReadFromReceiver()).count();

            return new RoomCardResponse(
                    account.getId(),
                    account.getProfileImageUrl(),
                    account.getStatus().toString(),
                    nickname,
                    lastMessage.getContent(),
                    room.getId(),
                    (lastMessage.getReceiver().getId().equals(account.getId()) ? 0L : unreadCount),
                    lastMessage.getCreatedAt().toString()
            );
        }
    }

    public record QuitRoomResponse(boolean result) {
    }

}
