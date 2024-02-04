package tinqle.tinqleServer.domain.block.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.block.dto.response.BlockResponseDto.BlockAccountResponse;
import tinqle.tinqleServer.domain.block.exception.BlockException;
import tinqle.tinqleServer.domain.block.model.Block;
import tinqle.tinqleServer.domain.block.repository.BlockRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {

    private final AccountService accountService;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository;
    private final BlockRepository blockRepository;

    @Transactional
    public BlockAccountResponse blockAccount(Long accountId, Long targetAccountId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(targetAccountId);

        boolean exists = blockRepository.existsByRequesterAccountAndBlockedAccountAndVisibilityIsTrue(loginAccount, targetAccount);
        if (exists)
            throw new BlockException(StatusCode.DUPLICATE_BLOCK_REQUEST);

        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriend(loginAccount, targetAccount);
        friendshipOptional.ifPresent(friendship -> friendshipService.deleteFriend(accountId, friendship.getId()));

        Block block = Block.builder()
                .requesterAccount(loginAccount)
                .blockedAccount(targetAccount).build();

        blockRepository.save(block);

        return new BlockAccountResponse(true);
    }
}
