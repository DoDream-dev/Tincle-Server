package tinqle.tinqleServer.domain.friendship.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipTemplate.createDummyFriendshipExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendshipRepositoryImplTest {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void clear() {
        accountRepository.deleteAll();
        friendshipRepository.deleteAll();
    }

    @Test
    @DisplayName("최근 순으로 나와 친구된 사람 조회 - 성공")
    public void findAllFriendshipByAccountSortCreatedAt_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        Account dummyAccountD = createDummyAccountExceptId("닉네임2");
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC,dummyAccountD));
        accountRepository.saveAll(dummyAccounts);

        Friendship dummyFriendshipWithB = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        friendshipRepository.save(dummyFriendshipWithB);
        Friendship dummyFriendshipWithC = createDummyFriendshipExceptId(dummyAccountA, dummyAccountC, true);
        friendshipRepository.save(dummyFriendshipWithC);
        Friendship dummyFriendshipWithD = createDummyFriendshipExceptId(dummyAccountA, dummyAccountD, true);
        friendshipRepository.save(dummyFriendshipWithD);
        Pageable pageable = PageRequest.of(0, 2);

        //when
        Slice<Friendship> pageOneFriendships = friendshipRepository.findAllFriendshipByAccountSortCreatedAt(dummyAccountA.getId(), pageable, 0L);
        Slice<Friendship> pageTwoFriendships = friendshipRepository.findAllFriendshipByAccountSortCreatedAt(dummyAccountA.getId(), pageable, dummyFriendshipWithC.getId());

        //then
        assertThat(pageOneFriendships.getSize()).isEqualTo(2);
        assertThat(pageOneFriendships.get().toList().get(0)).isEqualTo(dummyFriendshipWithD);   // 최근 순이니 D, C 조회
        assertThat(pageOneFriendships.get().toList().get(1)).isEqualTo(dummyFriendshipWithC);
        assertThat(pageTwoFriendships.get().toList().size()).isEqualTo(1);
        assertThat(pageTwoFriendships.get().toList().get(0)).isEqualTo(dummyFriendshipWithB);   // 페이지 크기가 2 이니 B는 두번째 페이지

    }

    @Test
    @DisplayName("내가 이름 바꾼 친구 조회 - 성공")
    public void findAllByAccountSelfAndIsChangeFriendNickname_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        Account dummyAccountD = createDummyAccountExceptId("닉네임2");
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC, dummyAccountD));
        accountRepository.saveAll(dummyAccounts);

        Friendship dummyFriendshipWithB = createDummyFriendshipExceptId(dummyAccountA, dummyAccountB, false);
        Friendship dummyFriendshipWithC = createDummyFriendshipExceptId(dummyAccountA, dummyAccountC, true);
        Friendship dummyFriendshipWithD = createDummyFriendshipExceptId(dummyAccountA, dummyAccountD, true);
        List<Friendship> dummyFriendships =
                new ArrayList<>(Arrays.asList(dummyFriendshipWithB, dummyFriendshipWithC, dummyFriendshipWithD));
        friendshipRepository.saveAll(dummyFriendships);

        //when
        List<Friendship> friends = friendshipRepository.findAllByAccountSelfAndIsChangeFriendNickname(dummyAccountA.getId(), true);

        //then
        assertThat(friends.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("나를 이름 바꾼 친구 조회 - 성공")
    public void findAllByAccountFriendAndIsChangeFriendNickname_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        Account dummyAccountD = createDummyAccountExceptId("닉네임2");
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC, dummyAccountD));
        accountRepository.saveAll(dummyAccounts);

        Friendship dummyFriendshipWithD_ByA = createDummyFriendshipExceptId(dummyAccountA, dummyAccountD, false);
        Friendship dummyFriendshipWithD_ByB = createDummyFriendshipExceptId(dummyAccountB, dummyAccountD, true);
        Friendship dummyFriendshipWithD_ByC = createDummyFriendshipExceptId(dummyAccountC, dummyAccountD, true);
        List<Friendship> dummyFriendships =
                new ArrayList<>(Arrays.asList(dummyFriendshipWithD_ByA, dummyFriendshipWithD_ByB, dummyFriendshipWithD_ByC));
        friendshipRepository.saveAll(dummyFriendships);

        //when
        List<Friendship> friends = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(dummyAccountD, true);

        //then
        assertThat(friends.size()).isEqualTo(2);
    }
}
