package tinqle.tinqleServer.domain.knock.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.config.TestQueryDslConfig;
import tinqle.tinqleServer.domain.knock.model.Knock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.createDummyAccountExceptId;
import static tinqle.tinqleServer.domain.knock.template.KnockTemplate.createKnockExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class KnockRepositoryTest {

    @Autowired
    private KnockRepository knockRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeAll
    public void init() {
        accountRepository.saveAll(dummyAccounts);
    }

    @AfterEach
    public void clear() {
        knockRepository.deleteAll();
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
            new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC,dummyAccountD));

    @Test
    @DisplayName("account에 따른 모든 sendAccount 조회 - 성공")
    public void findAllBySendAccount_success() throws Exception {
        //given
        Knock dummyKnockFromB = createKnockExceptId(dummyAccountA, dummyAccountB);
        Knock dummyKnockFromC = createKnockExceptId(dummyAccountA, dummyAccountC);
        Knock dummyKnockFromD = createKnockExceptId(dummyAccountB, dummyAccountD);

        List<Knock> dummyKnocks = new ArrayList<>(Arrays.asList(dummyKnockFromB, dummyKnockFromC, dummyKnockFromD));
        knockRepository.saveAll(dummyKnocks);

        //when
        List<Knock> knocks = knockRepository.findAllByAccount(dummyAccountA);

        //then
        assertThat(knocks.size()).isEqualTo(2);
        assertThat(knocks).containsExactlyInAnyOrder(dummyKnockFromB, dummyKnockFromC);
    }

    @Test
    @DisplayName("sendAccount가 동일한 account에게 보냈는지 확인 - 성공")
    public void existsByAccountAndSendAccount_success() throws Exception {
        //given
        Knock dummyKnockFromB = createKnockExceptId(dummyAccountA, dummyAccountB);
        Knock dummyKnockFromC = createKnockExceptId(dummyAccountA, dummyAccountB);
        knockRepository.save(dummyKnockFromB);
        knockRepository.save(dummyKnockFromC);

        //when
        boolean exists = knockRepository.existsByAccountAndSendAccountAndVisibilityIsTrue(dummyAccountA, dummyAccountB);

        //then
        assertThat(exists).isTrue();
    }
}
