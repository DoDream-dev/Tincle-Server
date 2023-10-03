package tinqle.tinqleServer.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.PageResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCheckedVo;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.exception.EmoticonException;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto.CreateFeedRequest;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.EmoticonCountAndChecked;
import tinqle.tinqleServer.domain.feed.exception.FeedException;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;
import tinqle.tinqleServer.domain.feed.repository.FeedImageRepository;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import java.util.List;

import static tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final AccountService accountService;
    private final FeedRepository feedRepository;
    private final EmoticonRepository emoticonRepository;
    private final FriendshipRepository friendshipRepository;
    private final FeedImageRepository feedImageRepository;
    private final FriendshipService friendshipService;

    //피드 조회
    public PageResponse<FeedCardResponse> getFeeds(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Slice<Feed> feeds = feedRepository.findAllByFriendWithMe(accountId, pageable, cursorId);
        List<Friendship> friendships = friendshipRepository
                .findAllByAccountSelfAndIsChangeFriendNickname(loginAccount.getId(), true);

        Slice<FeedCardResponse> result = feeds.map(feed -> FeedCardResponse.of(
                feed, friendshipService.getFriendNickname(friendships, feed.getAccount()),
                isFeedAuthor(loginAccount, feed), getEmoticonCountAndChecked(loginAccount,feed)));

        return PageResponse.of(result);
    }

    //피드 작성
    @Transactional
    public CreateFeedResponse createFeed(Long accountId, CreateFeedRequest createFeedRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = Feed.builder()
                .account(loginAccount)
                .content(createFeedRequest.content())
                .build();
        feedRepository.save(feed);

        List<String> imageUrls = createFeedRequest.feedImageUrl();

        imageUrls.forEach(imageUrl -> {
            FeedImage feedImage = FeedImage.builder()
                    .feed(feed)
                    .imageUrl(imageUrl)
                    .build();
            feedImageRepository.save(feedImage);
            feed.addFeedImage(feedImage);
        });

        return CreateFeedResponse.of(feed, loginAccount);
    }

    // 피드 삭제
    @Transactional
    public DeleteFeedResponse deleteFeed(Long accountId, Long feedId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = getFeedById(feedId);
        if (!loginAccount.getId().equals(feed.getId())) throw new FeedException(StatusCode.NOT_AUTHOR_FEED);

        feedRepository.delete(feed);    //12시간 후 자동 삭제는 soft delete이지만 사용자가 선택한 삭제는 hard delete

        boolean exists = feedRepository.existsById(feedId);
        return DeleteFeedResponse.of(exists);
    }

    public Feed getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new FeedException(StatusCode.NOT_FOUND_FEED));
        if (!feed.isVisibility()) throw new EmoticonException(StatusCode.IS_DELETED_FEED);
        return feed;
    }

    //피드 작성자인지 확인
    private boolean isFeedAuthor(Account account, Feed feed) {
        return feed.getAccount().getId().equals(account.getId());
    }

    //각 이모티콘 갯수 확인 및 체크 여부 확인
    private EmoticonCountAndChecked getEmoticonCountAndChecked(Account account, Feed feed) {
        List<EmoticonCountVo> emoticonCounts = emoticonRepository.countAllEmoticonTypeByFeedAndVisibleIsTrue(feed);
        if(emoticonCounts.isEmpty()) return EmoticonCountAndChecked.isEmpty();

        Long heartCount = getEmoticonCountByEmoticonType(emoticonCounts, "HEART");
        Long smileCount = getEmoticonCountByEmoticonType(emoticonCounts, "SMILE");
        Long sadCount = getEmoticonCountByEmoticonType(emoticonCounts, "SAD");
        Long surpriseCount = getEmoticonCountByEmoticonType(emoticonCounts, "SURPRISE");

        List<EmoticonCountVo> emoticonCountVoList = emoticonRepository.countAllEmoticonTypeByFeedAndAccountAndVisibleIsTrue(feed, account);

        EmoticonCheckedVo emoticonCheckedVo = EmoticonCheckedVo.of(emoticonCountVoList);


        return new EmoticonCountAndChecked(heartCount,smileCount,sadCount,surpriseCount,
                emoticonCheckedVo.isCheckedHeartEmoticon(),emoticonCheckedVo.isCheckedSmileEmoticon(),
                emoticonCheckedVo.isCheckedSadEmoticon(), emoticonCheckedVo.isCheckedSurpriseEmoticon());
    }

    private static Long getEmoticonCountByEmoticonType(List<EmoticonCountVo> emoticonCounts, String emoticonType) {
        return emoticonCounts.stream().filter(emoticonCountVo -> emoticonCountVo.getEmoticonType().equals(emoticonType))
                .findFirst()
                .orElse(new EmoticonCountVo(emoticonType,0L)).getCount();
    }
}
