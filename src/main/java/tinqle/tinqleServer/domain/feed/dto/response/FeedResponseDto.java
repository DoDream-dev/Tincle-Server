package tinqle.tinqleServer.domain.feed.dto.response;


import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;

import java.util.List;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveElapsedTime;

public class FeedResponseDto {

    public record FeedCardResponse(
            Long feedId,
            Long accountId,
            String status,
            String friendNickname,
            String content,
            List<String> feedImageUrls,
            Long commentCount,
            EmoticonCountAndChecked emoticons,
            boolean isAuthor,
            String createdAt
    ) {
        @Builder
        public FeedCardResponse {}

        public static FeedCardResponse of(Feed feed, String friendNickname, boolean isAuthor, EmoticonCountAndChecked emoticons) {
            return FeedCardResponse.builder()
                    .feedId(feed.getId())
                    .accountId(feed.getAccount().getId())
                    .status(feed.getAccount().getStatus().toString())
                    .friendNickname(friendNickname)
                    .content(feed.getContent())
                    .feedImageUrls(feed.getFeedImageList().stream().map(FeedImage::getImageUrl).toList())
                    .commentCount((long) feed.getCommentList().size())
                    .emoticons(emoticons)
                    .isAuthor(isAuthor)
                    .createdAt(resolveElapsedTime(feed.getCreatedAt())).build();
        }
    }

    public record EmoticonCountAndChecked(
            Long heartEmoticonCount,
            Long smileEmoticonCount,
            Long sadEmoticonCount,
            Long surpriseEmoticonCount,
            boolean isCheckedHeartEmoticon,
            boolean isCheckedSmileEmoticon,
            boolean isCheckedSadEmoticon,
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
            String status,
            boolean isAuthor,
            String createdAt
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
                    .status(account.getStatus().toString())
                    .isAuthor(true)
                    .createdAt(resolveElapsedTime(feed.getCreatedAt())).build();
        }
    }

    public record DeleteFeedResponse(
            boolean isDeleted
    ) {
        public static DeleteFeedResponse of(boolean exists) {
            return new DeleteFeedResponse(!exists);
        }
    }
}
