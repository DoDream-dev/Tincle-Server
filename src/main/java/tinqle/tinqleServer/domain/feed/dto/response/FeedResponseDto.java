package tinqle.tinqleServer.domain.feed.dto.response;


import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponseDto {

    public record FeedCardResponse(
            Long feedId,
            Long accountId,
            String statusImageUrl,
            String nickname,
            String friendNickname,
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
                    .feedId(feed.getId())
                    .accountId(feed.getAccount().getId())
                    .statusImageUrl(feed.getAccount().getStatus().getStatusImageUrl())
                    .nickname(feed.getAccount().getNickname())
                    .friendNickname(friendNickname)
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
            boolean isCheckedSmileEmoticon,
            boolean isCheckedSadEmoticon,
            boolean isCheckedHeartEmoticon,
            boolean isCheckedSurpriseEmoticon
    ) {
        public static EmoticonCountAndChecked isEmpty() {
            return new EmoticonCountAndChecked(0L,0L,0L,0L,
                    false,false,false,false);
        }
    }

    public record CreateFeedResponse(
            Long feedId,
            Long accountId,
            String nickname,
            String content,
            List<String> feedImageUrls,
            String statusImageUrl,
            boolean isAuthor,
            LocalDateTime createdAt
    ) {
        @Builder
        public CreateFeedResponse{}

        public static CreateFeedResponse of(Feed feed, Account account) {
            return CreateFeedResponse.builder()
                    .feedId(feed.getId())
                    .accountId(account.getId())
                    .nickname(account.getNickname())
                    .content(feed.getContent())
                    .feedImageUrls(feed.getFeedImageList().stream().map(FeedImage::getImageUrl).toList())
                    .statusImageUrl(account.getStatus().getStatusImageUrl())
                    .isAuthor(true)
                    .createdAt(feed.getCreatedAt()).build();
        }
    }
}
