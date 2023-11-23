package tinqle.tinqleServer.common.dummy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.model.FeedImage;
import tinqle.tinqleServer.domain.feed.repository.FeedImageRepository;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.util.ArrayList;
import java.util.List;

import static tinqle.tinqleServer.common.constant.GlobalConstants.ACCOUNT_STATUS_URL;

@Component("feedDummy")
@DependsOn("friendshipDummy")
@RequiredArgsConstructor
@Slf4j
@Transactional
@TinqleDummy
public class FeedDummy {

    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        if (feedRepository.count() > 0) {
            log.info("[feedDummy] 더미 피드가 이미 존재합니다.");
        } else {
            createFeeds();
            log.info("[feedDummy] 더미 피드 생성완료");
        }

        if (feedImageRepository.count() > 0) {
            log.info("[feedDummy] 더미 피드이미지가 이미 존재합니다.");
        } else {
            createFeedImages();
            log.info("[feedDummy] 더미 피드이미지 생성완료");
        }
    }



    private void createFeeds() {
        List<Account> accounts = accountRepository.findAll();

        ArrayList<String> contents = new ArrayList<>();
        contents.add("심심해");
        contents.add("지루해");
        contents.add("놀자");


        for (int i = 0 ; i < 50 ; i++) {
            Feed feed = Feed.builder()
                    .content(contents.get((int) (Math.random() * 100) % 3))
                    .account(accounts.get(i)).build();
            feedRepository.save(feed);
        }
    }

    private void createFeedImages() {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(ACCOUNT_STATUS_URL+"/status/smile.png");
        imageUrls.add(ACCOUNT_STATUS_URL+"/status/sad.png");
        imageUrls.add(ACCOUNT_STATUS_URL+"/status/happy.png");
        imageUrls.add(null);
        List<Feed> feeds = feedRepository.findAll();

        for (Feed feed : feeds) {
            FeedImage feedImage = FeedImage.builder()
                    .imageUrl(imageUrls.get((int) (Math.random() * 100) % 4))
                    .feed(feed)
                    .build();
            feedImageRepository.save(feedImage);
        }
    }
}
