package tinqle.tinqleServer.domain.messageBox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.messageBox.dto.response.MessageBoxResponseDto.MessageBoxResponse;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;
import tinqle.tinqleServer.domain.messageBox.repository.MessageBoxRepository;

import static tinqle.tinqleServer.domain.messageBox.dto.request.MessageBoxRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageBoxService {

    private final AccountService accountService;
    private final MessageBoxRepository messageBoxRepository;

    public SliceResponse<MessageBoxResponse> getMessageBox(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Slice<MessageBox> messageBoxes = messageBoxRepository.findAllByAccountCustom(loginAccount, pageable, cursorId);

        Slice<MessageBoxResponse> result = messageBoxes.map(MessageBoxResponse::of);

        return SliceResponse.of(result);
    }

    @Transactional
    public MessageBoxResponse createMessageBox(Long accountId, Long targetId, CreateMessageBoxRequest createMessageBoxRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(targetId);

        MessageBox messageBox = MessageBox.builder()
                .sendAccount(loginAccount)
                .receiveAccount(targetAccount)
                .message(createMessageBoxRequest.message()).build();

        messageBoxRepository.save(messageBox);

        return new MessageBoxResponse(messageBox.getId(), messageBox.getMessage(), messageBox.getCreatedAt());
    }
}