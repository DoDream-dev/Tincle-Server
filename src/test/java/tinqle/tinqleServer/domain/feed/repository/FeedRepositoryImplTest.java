package tinqle.tinqleServer.domain.feed.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.config.TestQueryDslConfig;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.feed.template.FeedTemplate.createDummyFeedExceptId;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipTemplate.createDummyFriendshipExceptId;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class FeedRepositoryImplTest {

    public Account dummyAccountA = createDummyAccountA_ExceptId();
    public Account dummyAccountB = createDummyAccountB_ExceptId();
    public Account dummyAccountC = createDummyAccountExceptId("닉네임");
    public Account dummyAccountD = createDummyAccountExceptId("닉네임2");
    public Account dummyAccountE = createDummyAccountExceptId("닉네임3");
    public List<Account> dummyAccounts =
            new ArrayList<>(Arrays.asList(dummyAccountA, dummyAccountB, dummyAccountC, dummyAccountD, dummyAccountE));
    public Friendship dummyFriendshipWithB = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
    public Friendship dummyFriendshipWithC = createDummyFriendshipExceptId(dummyAccountA, dummyAccountC, false);
    public Friendship dummyFriendshipWithD = createDummyFriendshipExceptId(dummyAccountA, dummyAccountD, false);
    public List<Friendship> dummyFriendships =
            new ArrayList<>(Arrays.asList(dummyFriendshipWithB, dummyFriendshipWithC, dummyFriendshipWithD));
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @BeforeAll
    public void init() {
        accountRepository.saveAll(dummyAccounts);
        friendshipRepository.saveAll(dummyFriendships);
    }

    @BeforeEach
    public void clear() {
        feedRepository.deleteAll();
    }

    @AfterAll
    public void clearAfterAll() {
        accountRepository.deleteAll();
        feedRepository.deleteAll();
        friendshipRepository.deleteAll();
    }

    @Test
    @DisplayName("나와 내 친구의 피드를 최신순으로 정렬(무한 페이징) - 성공")
    public void findAllByFriendWithMe_success() throws Exception {
        //given
        for (Account dummyAccount : dummyAccounts) {
            for (int cnt = 0; cnt < 3; cnt++) {
                Feed feed = createDummyFeedExceptId((dummyAccount.getId() + "작성 피드" + cnt), dummyAccount);
                feedRepository.save(feed);
            }
        }

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Slice<Feed> firstPageFeeds = feedRepository.findAllByFriendWithMe(dummyAccountA.getId(), pageable, 0L);
        Slice<Feed> secondPageFeeds = feedRepository.findAllByFriendWithMe(
                dummyAccountA.getId(), pageable, firstPageFeeds.getContent().get(pageable.getPageSize()-1).getId());
        Slice<Feed> thirdPageFeeds = feedRepository.findAllByFriendWithMe(
                dummyAccountA.getId(), pageable, secondPageFeeds.getContent().get(pageable.getPageSize()-1).getId());


        //then
        // E와는 친구가 아니어서 조회되면 안됨.
        assertThat(firstPageFeeds.getContent())
                .extracting(Feed::getAccount)
                        .doesNotContain(dummyAccountE);

        // 첫 번째 페이지 검증
        assertThat(firstPageFeeds.getContent())
                .hasSize(5)
                .extracting(Feed::getContent)
                .containsExactly(
                        dummyAccountD.getId()+"작성 피드2",
                        dummyAccountD.getId()+"작성 피드1",
                        dummyAccountD.getId()+"작성 피드0",
                        dummyAccountC.getId()+"작성 피드2",
                        dummyAccountC.getId()+"작성 피드1"
                );
        // 두 번째 페이지 검증
        assertThat(secondPageFeeds.getContent())
                .hasSize(5)
                .extracting(Feed::getContent)
                .containsExactly(
                        dummyAccountC.getId()+"작성 피드0",
                        dummyAccountB.getId()+"작성 피드2",
                        dummyAccountB.getId()+"작성 피드1",
                        dummyAccountB.getId()+"작성 피드0",
                        dummyAccountA.getId()+"작성 피드2"
                );
        // 세 번째 페이지 검증
        assertThat(thirdPageFeeds.getContent())
                .hasSize(2)
                .extracting(Feed::getContent)
                .containsExactly(
                        dummyAccountA.getId()+"작성 피드1",
                        dummyAccountA.getId()+"작성 피드0"
                );
    }
}
