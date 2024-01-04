package tinqle.tinqleServer.domain.account.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.config.TestQueryDslConfig;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private CommentRepository commentRepository;


    @BeforeEach
    public void clear() {
        accountRepository.deleteAll();
        feedRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("account 저장 - 성공")
    public void accountSave_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        accountRepository.save(dummyAccountA);
        accountRepository.save(dummyAccountB);

        //when
        List<Account> accounts = accountRepository.findAll();

        //then
        assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("socialEmail로 찾기 - 성공")
    public void findBySocialEmail_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        Account account = accountRepository.findBySocialEmail(dummyAccountA.getSocialEmail()).orElseThrow(() -> new IllegalArgumentException("에러"));

        //then
        assertThat(account.getSocialEmail()).isEqualTo(dummyAccountA.getSocialEmail());
        assertThat(account.getAccountStatus()).isEqualTo(dummyAccountA.getAccountStatus());
        assertThat(account.getCode()).isEqualTo(dummyAccountA.getCode());
    }

    @Test
    @DisplayName("해당 코드가 있는지 검색 - 성공")
    public void existsByCode_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        boolean exists = accountRepository.existsByCode(dummyAccountA.getCode());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("해당 socialEmail이 있는지 검색 - 성공")
    public void existsBySocialEmail_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        boolean exists = accountRepository.existsBySocialEmail(dummyAccountA.getSocialEmail());

        //then
        assertThat(exists).isTrue();
    }
}
