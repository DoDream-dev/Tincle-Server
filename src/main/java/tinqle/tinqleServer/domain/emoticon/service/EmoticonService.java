package tinqle.tinqleServer.domain.emoticon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.emoticon.dto.request.EmoticonRequestDto.EmoticonReactRequest;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.EmoticonReactResponse;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.GetNicknameListResponse;
import tinqle.tinqleServer.domain.emoticon.exception.EmoticonException;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmoticonService {

    public final EmoticonRepository emoticonRepository;
    public final AccountService accountService;
    public final FeedService feedService;
    public final FriendshipRepository friendshipRepository;
    public final FriendshipService friendshipService;

    // 피드에 이모티콘 반응한 사람들 조회
    public GetNicknameListResponse getEmoticonReactAccount(Long accountId, Long feedId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);

        if (!loginAccount.getId().equals(feed.getAccount().getId())) throw new EmoticonException(StatusCode.NOT_AUTHOR_FEED);

        List<Friendship> friendships = friendshipRepository
                .findAllByAccountSelfAndIsChangeFriendNickname(loginAccount.getId(), true);

        List<Emoticon> emoticons = emoticonRepository.findAllByFeedAndVisibleIsTrueAndFetchJoinAccount(feed);
        List<String> heartEmoticonNicknameList = getFriendNicknameByEmoticonType(friendships, emoticons, EmoticonType.HEART);
        List<String> smileEmoticonNicknameList = getFriendNicknameByEmoticonType(friendships, emoticons, EmoticonType.SMILE);
        List<String> sadEmoticonNicknameList = getFriendNicknameByEmoticonType(friendships, emoticons, EmoticonType.SAD);
        List<String> surpriseEmoticonNicknameList = getFriendNicknameByEmoticonType(friendships, emoticons, EmoticonType.SURPRISE);

        return new GetNicknameListResponse(
                heartEmoticonNicknameList,smileEmoticonNicknameList,sadEmoticonNicknameList,surpriseEmoticonNicknameList);
    }

    //닉네임 바꿨는지 여부에 따라 닉네임 반환 및 emoticonType에 따라 분류
    private List<String> getFriendNicknameByEmoticonType(List<Friendship> friendships, List<Emoticon> emoticons, EmoticonType emoticonType) {
        return emoticons.stream().filter(emoticon -> emoticon.getEmoticonType().equals(emoticonType))
                .map(emoticon -> friendshipService.getFriendNickname(friendships, emoticon.getAccount()))
                .toList();
    }

    //이모티콘 반응
    @Transactional
    public EmoticonReactResponse emoticonReact(Long accountId, Long feedId, EmoticonReactRequest emoticonReactRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);

        EmoticonType emoticonType = EmoticonType.toEnum(emoticonReactRequest.emoticonType());

        Optional<Emoticon> emoticonOptional = emoticonRepository.findByAccountAndFeedAndEmoticonType(loginAccount, feed, emoticonType);
        if (emoticonOptional.isEmpty()) {
            Emoticon emoticon = Emoticon.builder()
                    .account(loginAccount)
                    .feed(feed)
                    .emoticonType(emoticonType)
                    .build();
            emoticonRepository.save(emoticon);

            return new EmoticonReactResponse(true);
        }
        else {
            Emoticon emoticon = emoticonOptional.get();
            updateEmoticonVisibility(emoticon);
            return new EmoticonReactResponse(emoticon.isVisibility());
        }
    }

    private static void updateEmoticonVisibility(Emoticon emoticon) {
        if (emoticon.isVisibility()) {
            emoticon.softDelete();
        } else {
            emoticon.setVisible();
        }
    }
}
