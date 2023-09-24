package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.MyAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateNicknameResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateStatusResponse;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.Status;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final FriendshipRepository friendshipRepository;

    public MyAccountInfoResponse getMyAccountInfo(Long accountId) {
        Account account = getAccountById(accountId);
        return new MyAccountInfoResponse(account.getId(), account.getNickname(),account.getStatus().getStatusImageUrl());
    }

    public OthersAccountInfoResponse getOthersAccountInfo(Long accountId, Long getInfoTargetId) {
        Account loginAccount = getAccountById(accountId);
        Account targetAccount = getAccountById(getInfoTargetId);

        if (accountId.equals(getInfoTargetId)) return OthersAccountInfoResponse.of(getMyAccountInfo(accountId));

        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriend(loginAccount, targetAccount);

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            String targetNickname =
                    (friendship.getIsChangeFriendNickname()) ? friendship.getFriendNickname() : targetAccount.getNickname();

            return new OthersAccountInfoResponse(
                    targetAccount.getId(),targetNickname,targetAccount.getStatus().getStatusImageUrl(), true);
        } else {
            return new OthersAccountInfoResponse(
                    targetAccount.getId(), targetAccount.getNickname(), targetAccount.getStatus().getStatusImageUrl(), false
            );
        }
    }

    public OthersAccountInfoResponse searchByCode(Long accountId, String code) {
        checkAccountById(accountId);
        Optional<Account> accountOptional = accountRepository.findByCode(code);

        if (accountOptional.isEmpty()) throw new AccountException(StatusCode.NOT_FOUND_ACCOUNT_CODE);

        return getOthersAccountInfo(accountId,accountOptional.get().getId());
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));
    }

    public void checkAccountById(Long accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) throw new AccountException(StatusCode.NOT_FOUND_ACCOUNT);
    }

    @Transactional
    public UpdateNicknameResponse updateNickname(Long accountId,String nickname) {
        Account loginAccount = getAccountById(accountId);
//        validateNickname(nickname);
        if (nickname.equals(loginAccount.getNickname())) throw new AccountException(StatusCode.SAME_NICKNAME_ERROR);
        loginAccount.updateNickname(nickname);
        return new UpdateNicknameResponse(loginAccount.getNickname());
    }

    //필요 시 닉네임 검증 추가
    private void validateNickname(String nickname) {
        String pattern = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$";
        if (!Pattern.matches(pattern, nickname)) {
            throw new AccountException(StatusCode.NICKNAME_VALIDATE_ERROR);
        }
    }

    @Transactional
    public UpdateStatusResponse updateStatus(Long accountId, String paramStatus) {
        Account loginAccount = getAccountById(accountId);
        Status status = Status.toEnum(paramStatus);

        if (loginAccount.getStatus().equals(status)) throw new AccountException(StatusCode.SAME_STATUS_ERROR);

        loginAccount.updateStatus(status);

        return new UpdateStatusResponse(status.getStatusImageUrl());
    }

}
