package tinqle.tinqleServer.domain.room.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.room.model.Room;

import java.time.LocalDateTime;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveElapsedTime;

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
        public static RoomCardResponse of(Account account, String nickname, Room room, Long unreadCount, Message lastMessage) {
            return new RoomCardResponse(
                    account.getId(),
                    account.getProfileImageUrl(),
                    account.getStatus().toString(),
                    nickname,
                    lastMessage.getContent(),
                    room.getId(),
                    (lastMessage.getReceiver().getId().equals(account.getId()) ? 0L : unreadCount),
                    resolveElapsedTime(lastMessage.getCreatedAt())
            );
        }
    }

    public record MessageCardResponse(
            Long messageId,
            String content,
            boolean isAuthor,
            @JsonFormat(pattern = "yyyy.M.dd (E) HH:mm")
            LocalDateTime createdAt
    ) {
        public static MessageCardResponse of(Message message, boolean isAuthor) {
            return new MessageCardResponse(
                    message.getId(),
                    message.getContent(),
                    isAuthor,
                    message.getCreatedAt()
            );
        }
    }

    public record GetAccountInfoResponse(
            OthersAccountInfoResponse othersAccountInfoResponse,
            Long accountId) {}

    public record QuitRoomResponse(boolean result) {
    }

}
