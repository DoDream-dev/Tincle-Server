package tinqle.tinqleServer.domain.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tinqle.tinqleServer.domain.message.dto.request.MessageRequest.SaveMessageRequest;
import tinqle.tinqleServer.domain.message.service.MessageService;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/message")
    public void message(@DestinationVariable final Long roomId, SaveMessageRequest saveMessageRequest) {
        messageService.createMessage(roomId, saveMessageRequest);
    }
}
