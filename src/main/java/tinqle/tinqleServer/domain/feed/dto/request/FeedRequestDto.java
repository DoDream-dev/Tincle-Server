package tinqle.tinqleServer.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FeedRequestDto {

    public record CreateFeedRequest(
            @NotBlank
            String content,
            List<String> feedImageUrl,
            @NotNull
            Boolean isReceivedEmoticon
    ) {}
}
