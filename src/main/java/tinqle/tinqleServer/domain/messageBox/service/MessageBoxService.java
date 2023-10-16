package tinqle.tinqleServer.domain.messageBox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.messageBox.dto.response.MessageBoxResponseDto.CreateMessageBoxResponse;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;
import tinqle.tinqleServer.domain.messageBox.repository.MessageBoxRepository;

import static tinqle.tinqleServer.domain.messageBox.dto.request.MessageBoxRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageBoxService {

    private final AccountService accountService;
    private final MessageBoxRepository messageBoxRepository;

    public CreateMessageBoxResponse createMessageBox(Long accountId, Long targetId, CreateMessageBoxRequest createMessageBoxRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(targetId);

        MessageBox messageBox = MessageBox.builder()
                .sendAccount(loginAccount)
                .receiveAccount(targetAccount)
                .message(createMessageBoxRequest.message()).build();

        messageBoxRepository.save(messageBox);

        return new CreateMessageBoxResponse(messageBox.getId(), messageBox.getMessage());
    }
}
