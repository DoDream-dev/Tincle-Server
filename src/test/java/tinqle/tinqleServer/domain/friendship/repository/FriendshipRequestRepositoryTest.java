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
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.createDummyAccountExceptId;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipRequestTemplate.createDummyFriendshipRequestExceptId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class FriendshipRequestRepositoryTest {

    @Autowired
    FriendshipRequestRepository friendshipRequestRepository;
    @Autowired
    AccountRepository accountRepository;

    @BeforeAll
    public void init() {
        accountRepository.saveAll(dummyAccounts);
    }

    @BeforeEach
    public void clear() {
        friendshipRequestRepository.deleteAll();
    }

    @AfterAll
    public void clearAfterAll() {
        accountRepository.deleteAll();
        friendshipRequestRepository.deleteAll();
    }

    public Account dummyAccountA = createDummyAccountA_ExceptId();
    public Account dummyAccountB = createDummyAccountB_ExceptId();
    public Account dummyAccountC = createDummyAccountExceptId("닉네임");
    public Account dummyAccountD = createDummyAccountExceptId("닉네임2");
    public List<Account> dummyAccounts =
            new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC,dummyAccountD));


    @Test
    @DisplayName("나와 친구 요청한 상태 조회 - 성공")
    public void existsByRequestAccountAndResponseAccountAndRequestStatus_success() throws Exception {
        //given
        FriendshipRequest friendshipRequestWaiting = createDummyFriendshipRequestExceptId(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        friendshipRequestRepository.save(friendshipRequestWaiting);

        //when
        boolean exists = friendshipRequestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        boolean exists_False = friendshipRequestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(dummyAccountA, dummyAccountB, RequestStatus.APPROVE);

        //then
        assertThat(exists).isTrue();
        assertThat(exists_False).isFalse();
    }
}
