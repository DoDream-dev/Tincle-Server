package tinqle.tinqleServer.domain.message.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import tinqle.tinqleServer.domain.message.model.Message;

import java.time.LocalDateTime;

public record MessageResponse(
    Long accountId,
    String content,
    @JsonFormat(pattern = "yyyy.M.dd (E) HH:mm")
    LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getSender().getId(), message.getContent(), message.getCreatedAt());
    }
}
