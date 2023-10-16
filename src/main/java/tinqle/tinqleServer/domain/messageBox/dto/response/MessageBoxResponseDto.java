package tinqle.tinqleServer.domain.messageBox.dto.response;

public class MessageBoxResponseDto {

    public record CreateMessageBoxResponse(Long messageBoxId, String message) {}
}
