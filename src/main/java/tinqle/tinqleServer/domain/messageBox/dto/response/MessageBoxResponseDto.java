package tinqle.tinqleServer.domain.messageBox.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;

import java.time.LocalDateTime;

public class MessageBoxResponseDto {

    public record MessageBoxResponse(Long messageBoxId, String message, LocalDateTime createdAt) {

        @Builder
        public MessageBoxResponse{}

        public static MessageBoxResponse of(MessageBox messageBox) {
            return MessageBoxResponse.builder()
                    .messageBoxId(messageBox.getId())
                    .message(messageBox.getMessage())
                    .createdAt(messageBox.getCreatedAt()).build();
        }

    }

}
