package tinqle.tinqleServer.domain.messageBox.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveDateFromDateTime;

public class MessageBoxResponseDto {

    public record MessageBoxResponse(Long messageBoxId, String message, String createdAt) {

        @Builder
        public MessageBoxResponse{}

        public static MessageBoxResponse of(MessageBox messageBox) {
            return MessageBoxResponse.builder()
                    .messageBoxId(messageBox.getId())
                    .message(messageBox.getMessage())
                    .createdAt(resolveDateFromDateTime(messageBox.getCreatedAt())).build();
        }

    }

}
