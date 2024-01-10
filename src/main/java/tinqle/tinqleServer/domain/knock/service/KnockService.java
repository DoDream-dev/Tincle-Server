package tinqle.tinqleServer.domain.knock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.knock.dto.request.KnockRequestDto.SendKnockRequest;
import tinqle.tinqleServer.domain.knock.dto.response.KnockResponseDto.SendKnockResponse;
import tinqle.tinqleServer.domain.knock.model.Knock;
import tinqle.tinqleServer.domain.knock.exception.KnockException;
import tinqle.tinqleServer.domain.knock.repository.KnockRepository;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KnockService {

    private final AccountService accountService;
    private final FriendshipService friendshipService;
    private final NotificationService notificationService;
    private final KnockRepository knockRepository;

    @Transactional
    public SendKnockResponse sendKnock(Long accountId, SendKnockRequest sendKnockRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(sendKnockRequest.targetAccountId());

        validateDuplicateRequest(loginAccount, targetAccount);

        Friendship friendship = friendshipService.getFriendshipByAccountSelfAndAccountFriend(targetAccount, loginAccount);
        String friendNickname = friendshipService.getFriendNickname(friendship);
        Knock buildKnock = Knock.builder()
                .account(targetAccount)
                .sendAccount(loginAccount)
                .build();
        Knock knock = knockRepository.save(buildKnock);
        notificationService.pushMessage(NotificationDto.NotifyParams.ofSendKnockMessage(
                targetAccount, loginAccount, friendNickname
        ));

        return SendKnockResponse.of(knock);
    }

    private void validateDuplicateRequest(Account sendAccount, Account targetAccount) {
        boolean exists = knockRepository.existsByAccountAndSendAccountAndVisibilityIsTrue(targetAccount, sendAccount);
        if (!exists) return;

        throw new KnockException(StatusCode.DUPLICATE_KNOCK_REQUEST);
    }

    public List<Knock> getAllKnockByAccountAndVisibilityIsTrue(Account account) {
        List<Knock> knocks = knockRepository.findAllByAccountAndVisibilityIsTrue(account);
        if (knocks.isEmpty())
            throw new KnockException(StatusCode.NOT_FOUND_KNOCK);
        return knocks;
    }
}
