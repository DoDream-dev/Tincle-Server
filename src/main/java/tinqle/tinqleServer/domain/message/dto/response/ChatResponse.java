package tinqle.tinqleServer.domain.message.dto.response;

import tinqle.tinqleServer.domain.message.model.Message;

public record ChatResponse(
        Long roomId,
        MessageResponse messageResponse
) {
    public static ChatResponse of(Long roomId, Message message) {
        return new ChatResponse(roomId, MessageResponse.from(message));
    }
}
