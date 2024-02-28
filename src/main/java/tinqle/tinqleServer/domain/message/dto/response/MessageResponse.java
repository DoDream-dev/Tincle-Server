package tinqle.tinqleServer.domain.message.dto.response;

import tinqle.tinqleServer.domain.message.model.Message;

public record MessageResponse(
    Long accountId,
    String content,
    String createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getSender().getId(), message.getContent(), message.getCreatedAt().toString());
    }
}
