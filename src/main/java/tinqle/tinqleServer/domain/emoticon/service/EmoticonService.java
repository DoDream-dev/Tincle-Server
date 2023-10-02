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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmoticonService {

    public final EmoticonRepository emoticonRepository;
    public final AccountService accountService;
    public final FeedService feedService;

//    public GetNicknameListResponse getEmoticonReactAccount(Long accountId, Long feedId) {
//        Account loginAccount = accountService.getAccountById(accountId);
//        Feed feed = feedService.getFeedById(feedId);
//
//        if (!loginAccount.getId().equals(feed.getAccount().getId())) throw new EmoticonException(StatusCode.NOT_AUTHOR_FEED);
//
//    }

    @Transactional
    public EmoticonReactResponse emoticonReact(Long accountId, Long feedId, EmoticonReactRequest emoticonReactRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);
        if (!feed.isVisibility()) throw new EmoticonException(StatusCode.IS_DELETED_FEED);

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
