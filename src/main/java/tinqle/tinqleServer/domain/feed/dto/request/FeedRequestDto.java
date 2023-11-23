package tinqle.tinqleServer.domain.feed.dto.request;

import jakarta.validation.constraints.Size;

import java.util.List;

import static tinqle.tinqleServer.common.constant.ValidConstants.FEED_TEXT_LENGTH;

public class FeedRequestDto {

    public record CreateFeedRequest(
            @Size(max = FEED_TEXT_LENGTH)
            String content,
            List<String> feedImageUrl
    ) {}
}
