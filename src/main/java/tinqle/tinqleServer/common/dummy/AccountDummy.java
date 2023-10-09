package tinqle.tinqleServer.common.dummy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.*;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.util.UuidGenerateUtil;


@Component("accountDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountDummy {

    private final AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        if (accountRepository.count() > 0) {
            log.info("[accountDummy] 더미 계정이 이미 존재합니다.");
        } else {
            createAccounts();
            log.info("[accountDummy] 더미 계정 생성완료");
        }
    }

    private void createAccounts() {
        for (int i = 0; i < 50; i++) {
            Account account = Account.builder()
                    .role(Role.ROLE_USER)
                    .accountStatus(AccountStatus.NORMAL)
                    .socialType(SocialType.KAKAO)
                    .status(Status.HAPPY)
                    .password("password" + i)
                    .code(UuidGenerateUtil.makeRandomUuid())
                    .socialEmail("socialEmail@" + i)
                    .nickname("test" + (i+1))
                    .fcmToken(" ")
                    .isReceivedPushNotification(true)
                    .build();
            accountRepository.save(account);
        }
    }
}