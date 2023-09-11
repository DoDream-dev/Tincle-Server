package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.MyAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final FriendshipRepository friendshipRepository;

    public MyAccountInfoResponse getMyAccountInfo(Long accountId) {
        Account account = getAccountById(accountId);
        return new MyAccountInfoResponse(account.getNickname(),account.getStatus().getStatusImageUrl());
    }

    public OthersAccountInfoResponse getOthersAccountInfo(Long accountId, Long getInfoTargetId) {
        Account loginAccount = getAccountById(accountId);
        Account targetAccount = getAccountById(getInfoTargetId);
        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriend(loginAccount, targetAccount);

        /**
         * 만약 자기 아이디를 조회한다면 ???? -> exception or 반환 값 수정
         */
        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            String targetNickname =
                    (friendship.isChangeFriendNickname()) ? friendship.getFriendNickname() : targetAccount.getNickname();

            return new OthersAccountInfoResponse(
                    targetAccount.getId(),targetNickname,targetAccount.getStatus().getStatusImageUrl(), true);
        } else {
            return new OthersAccountInfoResponse(
                    targetAccount.getId(), targetAccount.getNickname(), targetAccount.getStatus().getStatusImageUrl(), false
            );
        }
    }

    public OthersAccountInfoResponse searchByCode(Long accountId, String code) {
        getAccountById(accountId);
        Optional<Account> accountOptional = accountRepository.findByCode(code);
        return (accountOptional.isPresent()) ? getOthersAccountInfo(accountId,accountOptional.get().getId()) :
                new OthersAccountInfoResponse(null, null, null, null);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));
    }
}
