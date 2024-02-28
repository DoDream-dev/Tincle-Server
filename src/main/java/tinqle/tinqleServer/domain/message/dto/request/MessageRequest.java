package tinqle.tinqleServer.domain.message.dto.request;

public class MessageRequest {

    public record SaveMessageRequest(
            Long accountId,
            String content
    ) {}
}
