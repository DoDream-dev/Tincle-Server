package tinqle.tinqleServer.domain.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.message.dto.request.MessageRequest.SaveMessageRequest;
import tinqle.tinqleServer.domain.message.dto.response.MessageCountResponse;
import tinqle.tinqleServer.domain.message.service.MessageService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@Controller
@Tag(name = TAG_MESSAGE, description = TAG_MESSAGE_DESCRIPTION)
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/message")
    public void message(@DestinationVariable final Long roomId, SaveMessageRequest saveMessageRequest) {
        messageService.createMessage(roomId, saveMessageRequest);
    }

    @ResponseBody
    @Operation(summary = GET_TOTAL_MESSAGE_COUNT)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/messages/count")
    public ApiResponse<MessageCountResponse> getMessageCount(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(messageService.getMessageCount(principalDetails.getId()));
    }
}
