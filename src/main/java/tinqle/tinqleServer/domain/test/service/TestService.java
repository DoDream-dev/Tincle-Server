package tinqle.tinqleServer.domain.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final AccountRepository accountRepository;
    private final FriendshipRepository friendshipRepository;
    private final AccountService accountService;

    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).get();
        accountRepository.delete(account);
    }

    @Transactional
    public void createFriendship(Long requestAccountId, Long accountId) {
        Account loginAccount = accountService.getAccountById(requestAccountId);
        Account requestAccount = accountService.getAccountById(accountId);

        Friendship loginAccountFriendship = Friendship.builder()
                .accountSelf(loginAccount)
                .accountFriend(requestAccount)
                .friendNickname(" ")
                .isChangeFriendNickname(false)
                .build();
        Friendship requestAccountFriendship = Friendship.builder()
                .accountSelf(requestAccount)
                .accountFriend(loginAccount)
                .friendNickname(" ")
                .isChangeFriendNickname(false)
                .build();

        friendshipRepository.save(loginAccountFriendship);
        friendshipRepository.save(requestAccountFriendship);
    }
}
