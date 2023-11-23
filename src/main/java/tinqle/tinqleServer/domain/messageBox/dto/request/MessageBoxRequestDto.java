package tinqle.tinqleServer.domain.messageBox.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static tinqle.tinqleServer.common.constant.ValidConstants.MESSAGE_BOX_LENGTH;

public class MessageBoxRequestDto {

    public record CreateMessageBoxRequest(
            @NotNull @Size(max = MESSAGE_BOX_LENGTH)
            String message) {}
}
