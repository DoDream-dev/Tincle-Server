package tinqle.tinqleServer.domain.feed.dto.response;


import lombok.Builder;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponseDto {

    public record FeedCardResponse(
            Long accountId,
            String statusImageUrl,
            String nickname,
            String friendNickname,
            Long feedId,
            String content,
            List<String> feedImageUrls,
            Long commentCount,
            EmoticonCountAndChecked emoticons,
            boolean isAuthor,
            LocalDateTime createdAt
    ) {
        @Builder
        public FeedCardResponse {}

        public static FeedCardResponse of(Feed feed, String friendNickname, boolean isAuthor, EmoticonCountAndChecked emoticons) {
            return FeedCardResponse.builder()
                    .accountId(feed.getAccount().getId())
                    .statusImageUrl(feed.getAccount().getStatus().getStatusImageUrl())
                    .nickname(feed.getAccount().getNickname())
                    .friendNickname(friendNickname)
                    .feedId(feed.getId())
                    .content(feed.getContent())
                    .feedImageUrls(feed.getFeedImageList().stream().map(FeedImage::getImageUrl).toList())
                    .commentCount((long) feed.getCommentList().size())
                    .emoticons(emoticons)
                    .isAuthor(isAuthor)
                    .createdAt(feed.getCreatedAt()).build();
        }
    }

    public record EmoticonCountAndChecked(
            Long smileEmoticonCount,
            Long sadEmoticonCount,
            Long heartEmoticonCount,
            Long surpriseEmoticonCount,
            boolean isSmileEmoticonChecked,
            boolean isSadEmoticonChecked,
            boolean isHeartEmoticonChecked,
            boolean isSurpriseEmoticonChecked
    ) {
        public static EmoticonCountAndChecked isEmpty() {
            return new EmoticonCountAndChecked(0L,0L,0L,0L,
                    false,false,false,false);
        }

    }
}
