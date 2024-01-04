package tinqle.tinqleServer.domain.friendship.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.config.TestQueryDslConfig;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipTemplate.createDummyFriendshipExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private AccountRepository accountRepository;
    @BeforeAll
    public void init() {
        accountRepository.saveAll(dummyAccounts);
    }

    @BeforeEach
    public void clear() {
        friendshipRepository.deleteAll();
    }

    @AfterAll
    public void clearAfterAll() {
        accountRepository.deleteAll();
    }

    public Account dummyAccountA = createDummyAccountA_ExceptId();
    public Account dummyAccountB = createDummyAccountB_ExceptId();
    public Account dummyAccountC = createDummyAccountExceptId("닉네임");
    public Account dummyAccountD = createDummyAccountExceptId("닉네임2");
    public List<Account> dummyAccounts =
            new ArrayList<>(Arrays.asList(dummyAccountA, dummyAccountB, dummyAccountC, dummyAccountD));

    @Test
    @DisplayName("나와 친구로 조회")
    public void findByAccountSelfAndAccountFriend_success() throws Exception {
        //given
        Friendship dummyFriendship = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        friendshipRepository.save(dummyFriendship);

        //when
        Optional<Friendship> optionalFriendship = friendshipRepository.findByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB);

        //then
        assertThat(optionalFriendship).isPresent();
        Friendship friendship = optionalFriendship.get();
        assertThat(friendship.getAccountSelf()).isEqualTo(dummyAccountA);
        assertThat(friendship.getAccountFriend()).isEqualTo(dummyAccountB);
    }

    @Test
    @DisplayName("나와 친구의 친구 여부 조회 - 성공")
    public void existsByAccountSelfAndAccountFriend_success() throws Exception {
        //given
        Friendship dummyFriendship = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        friendshipRepository.save(dummyFriendship);

        //when
        boolean exists = friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("나와 친구 ID로 친구 여부 조회 - 성공")
    public void existsByAccountSelfIdAndAccountFriendId_success() throws Exception {
        //given
        Friendship dummyFriendship = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        friendshipRepository.save(dummyFriendship);

        //when
        boolean exists = friendshipRepository.existsByAccountSelfIdAndAccountFriendId(dummyAccountA.getId(), dummyAccountB.getId());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("친구 이름을 바꾼 적이 있는 지 조회 - 성공")
    public void findByAccountSelfAndAccountFriendAndIsChangeFriendNickname_success() throws Exception {
        //given
        Friendship dummyFriendshipWithB = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        Friendship dummyFriendshipWithC = createDummyFriendshipExceptId(dummyAccountA, dummyAccountC, true);
        List<Friendship> dummyFriendships =
                new ArrayList<>(Arrays.asList(dummyFriendshipWithB, dummyFriendshipWithC));
        friendshipRepository.saveAll(dummyFriendships);

        //when
        Optional<Friendship> optionalFriendshipWithB = friendshipRepository.findByAccountSelfAndAccountFriendAndIsChangeFriendNickname(dummyAccountA, dummyAccountB, true);
        Optional<Friendship> optionalFriendshipWithC = friendshipRepository.findByAccountSelfAndAccountFriendAndIsChangeFriendNickname(dummyAccountA, dummyAccountC, true);

        //then
        assertThat(optionalFriendshipWithB).isEmpty();
        assertThat(optionalFriendshipWithC).isPresent();
    }
}
