package tinqle.tinqleServer.common.dummy;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.util.ArrayList;
import java.util.List;

@Component("emoticonDummy")
@DependsOn("feedDummy")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmoticonDummy {

    private final FeedRepository feedRepository;
    private final AccountRepository accountRepository;
    private final EmoticonRepository emoticonRepository;

    @PostConstruct
    public void init() {
        if (emoticonRepository.count() > 0) {
            log.info("[emoticonDummy] 더미 이모티콘이 이미 존재합니다.");
        } else {
            createEmoticons();
            log.info("[emoticonDummy] 더미 이모티콘 생성완료");
        }
    }

    private void createEmoticons() {
        List<Account> accounts = accountRepository.findAll();
        List<Feed> feeds = feedRepository.findAll();

        ArrayList<EmoticonType> emoticonTypes = new ArrayList<>();
        emoticonTypes.add(EmoticonType.SMILE);
        emoticonTypes.add(EmoticonType.SURPRISE);
        emoticonTypes.add(EmoticonType.SAD);
        emoticonTypes.add(EmoticonType.HEART);



        for (int i = 0 ; i<30; i++) {
            for (Feed feed : feeds) {
            Emoticon emoticon = Emoticon.builder()
                    .account(accounts.get(i))
                    .emoticonType(emoticonTypes.get((int) (Math.random() * 100) % 4))
                    .feed(feed)
                    .build();
            emoticonRepository.save(emoticon);
            }
        }
    }

}
