package tinqle.tinqleServer.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.PageResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.emoticon.dto.dao.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.EmoticonCountAndChecked;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.List;
import java.util.Map;
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

    //피드 조회
    public PageResponse<FeedCardResponse> getFeeds(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Slice<Feed> feeds = feedRepository.findAllByFriendWithMe(accountId, pageable, cursorId);
        Slice<FeedCardResponse> result = feeds.map(feed -> FeedCardResponse.of(
                feed, getFriendNickname(loginAccount,feed.getAccount()),isFeedAuthor(loginAccount, feed), getEmoticonCountAndChecked(loginAccount,feed)));

        return PageResponse.of(result);
    }

    private boolean isFeedAuthor(Account account, Feed feed) {
        return feed.getAccount().getId().equals(account.getId());
    }

    private EmoticonCountAndChecked getEmoticonCountAndChecked(Account account, Feed feed) {
        List<EmoticonCountVo> emoticonCounts = emoticonRepository.countEmoticonTypeByFeed(feed);
        if(emoticonCounts.isEmpty()) return EmoticonCountAndChecked.isEmpty();

        Long smileCount = null, sadCount = null, surpriseCount = null, heartCount = null;
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
        Map<EmoticonType, Boolean> checkedEmoticonResult = emoticonRepository.isCheckedEmoticonByFeedAndAccount(feed, account);

        return new EmoticonCountAndChecked(smileCount,sadCount,heartCount,surpriseCount,
                checkedEmoticonResult.get(EmoticonType.SMILE), checkedEmoticonResult.get(EmoticonType.SAD),
                checkedEmoticonResult.get(EmoticonType.HEART), checkedEmoticonResult.get(EmoticonType.SURPRISE));
    }

    private String getFriendNickname(Account loginAccount, Account friendAccount) {
        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriendAndIsChangeFriendNickname(loginAccount, friendAccount, true);
        return friendshipOptional.map(Friendship::getFriendNickname).orElse(null);
    }
}
