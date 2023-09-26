package tinqle.tinqleServer.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.PageResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCheckedVo;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto.CreateFeedRequest;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.EmoticonCountAndChecked;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;
import tinqle.tinqleServer.domain.feed.repository.FeedImageRepository;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.List;
import java.util.Optional;

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

    //피드 조회
    public PageResponse<FeedCardResponse> getFeeds(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Slice<Feed> feeds = feedRepository.findAllByFriendWithMe(accountId, pageable, cursorId);
        Slice<FeedCardResponse> result = feeds.map(feed -> FeedCardResponse.of(
                feed, getFriendNickname(loginAccount,feed.getAccount()),isFeedAuthor(loginAccount, feed), getEmoticonCountAndChecked(loginAccount,feed)));

        return PageResponse.of(result);
    }

    //피드 작성
    @Transactional
    public CreateFeedResponse createFeed(Long accountId, CreateFeedRequest createFeedRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = Feed.builder()
                .account(loginAccount)
                .content(createFeedRequest.content())
                .isReceivedEmoticon(createFeedRequest.isReceivedEmoticon())
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

    //피드 작성자인지 확인
    private boolean isFeedAuthor(Account account, Feed feed) {
        return feed.getAccount().getId().equals(account.getId());
    }

    //각 이모티콘 갯수 확인 및 체크 여부 확인
    private EmoticonCountAndChecked getEmoticonCountAndChecked(Account account, Feed feed) {
        List<EmoticonCountVo> emoticonCounts = emoticonRepository.countAllEmoticonTypeByFeed(feed);
        if(emoticonCounts.isEmpty()) return EmoticonCountAndChecked.isEmpty();

        Long smileCount = 0L, sadCount = 0L, surpriseCount = 0L, heartCount = 0L;
        for (EmoticonCountVo emoticonCount : emoticonCounts) {
            String emoticonType = emoticonCount.getEmoticonType();
            Long count = emoticonCount.getCount();
            switch (emoticonType) {
                case "SMILE" -> smileCount=count;
                case "SAD" -> sadCount=count;
                case "HEART" -> heartCount=count;
                case "SURPRISE" -> surpriseCount=count;
            }
        }
        List<EmoticonCountVo> emoticonCountVoList = emoticonRepository.countAllEmoticonTypeByFeedAndAccount(feed, account);

        EmoticonCheckedVo emoticonCheckedVo = EmoticonCheckedVo.of(emoticonCountVoList);


        return new EmoticonCountAndChecked(smileCount,sadCount,heartCount,surpriseCount,
                emoticonCheckedVo.isCheckedSmileEmoticon(), emoticonCheckedVo.isCheckedSadEmoticon(),
                emoticonCheckedVo.isCheckedHeartEmoticon(), emoticonCheckedVo.isCheckedSurpriseEmoticon());
    }

    // 친구 닉네임 변경시 친구 닉네임 가져오기
    private String getFriendNickname(Account loginAccount, Account friendAccount) {
        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriendAndIsChangeFriendNickname(loginAccount, friendAccount, true);
        return friendshipOptional.map(Friendship::getFriendNickname).orElse(null);
    }
}
