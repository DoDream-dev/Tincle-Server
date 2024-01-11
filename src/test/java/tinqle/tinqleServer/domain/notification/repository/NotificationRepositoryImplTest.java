package tinqle.tinqleServer.domain.notification.repository;

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
import tinqle.tinqleServer.domain.notification.model.Notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.createDummyAccountExceptId;
import static tinqle.tinqleServer.domain.notification.template.NotificationTemplate.createDummyNotificationExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class NotificationRepositoryImplTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeAll
    public void init() {
        accountRepository.saveAll(dummyAccounts);
    }

    @BeforeEach
    public void clear() {
        notificationRepository.deleteAll();
    }

    @AfterAll
    public void clearAfterAll() {
        notificationRepository.deleteAll();
        accountRepository.deleteAll();
    }

    public Account dummyAccountA = createDummyAccountA_ExceptId();
    public Account dummyAccountB = createDummyAccountB_ExceptId();
    public Account dummyAccountC = createDummyAccountExceptId("닉네임");
    public Account dummyAccountD = createDummyAccountExceptId("닉네임2");
    public List<Account> dummyAccounts =
            new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC,dummyAccountD));

    @Test
    @DisplayName("삭제하지 않은 알림들 최신순으로 정렬 후 조회 - 성공")
    public void findByAccountAndSortByLatest_success() throws Exception {
        //given
        for (Account dummyAccount : dummyAccounts) {
            if (dummyAccount.equals(dummyAccountA)) continue;
            for (int cnt = 1; cnt < 4; cnt++) {
                Notification dummyNotificationExceptId = createDummyNotificationExceptId(dummyAccountA, dummyAccount, dummyAccount.getId()+"의 "+cnt+"번 째 알림", false);
                notificationRepository.save(dummyNotificationExceptId);
            }
        }

        Pageable pageable = PageRequest.of(0, 3);

        //when
        Slice<Notification> firstPageNotifications = notificationRepository.findByAccountAndSortByLatest(dummyAccountA, pageable, 0L);
        Slice<Notification> secondPageNotifications = notificationRepository.findByAccountAndSortByLatest(dummyAccountA, pageable, firstPageNotifications.getContent().get(pageable.getPageSize()-1).getId());
        Slice<Notification> thirdPageNotifications = notificationRepository.findByAccountAndSortByLatest(dummyAccountA, pageable, secondPageNotifications.getContent().get(pageable.getPageSize()-1).getId());

        //then
        assertThat(firstPageNotifications.getContent())
                .hasSize(3)
                .extracting(Notification::getContent)
                .containsExactly(dummyAccountD.getId()+"의 "+3+"번 째 알림",
                        dummyAccountD.getId()+"의 "+2+"번 째 알림",
                        dummyAccountD.getId()+"의 "+1+"번 째 알림");
        assertThat(secondPageNotifications.getContent())
                .hasSize(3)
                .extracting(Notification::getContent)
                .containsExactly(dummyAccountC.getId()+"의 "+3+"번 째 알림",
                        dummyAccountC.getId()+"의 "+2+"번 째 알림",
                        dummyAccountC.getId()+"의 "+1+"번 째 알림");
        assertThat(thirdPageNotifications.getContent())
                .hasSize(3)
                .extracting(Notification::getContent)
                .containsExactly(dummyAccountB.getId()+"의 "+3+"번 째 알림",
                        dummyAccountB.getId()+"의 "+2+"번 째 알림",
                        dummyAccountB.getId()+"의 "+1+"번 째 알림");
    }
}
