package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.AccountInfoResponse;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountInfoResponse getAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));
        return new AccountInfoResponse(account.getNickname(),account.getStatus().getStatusImageUrl());
    }
}
