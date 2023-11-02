package tinqle.tinqleServer.domain.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.dto.request.FriendshipRequestDto.ChangeFriendNicknameRequest;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.*;
import tinqle.tinqleServer.domain.friendship.exception.FriendshipException;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendshipService {

    private final AccountService accountService;
    private final FriendshipRepository friendshipRepository;

    public CodeResponse getCode(Long accountId) {
        Account loginAccount = accountService.getAccountById(accountId);
        return new CodeResponse(loginAccount.getCode());
    }



    public SliceResponse<FriendshipCardResponse> getFriendshipManage(Long accountId, Pageable pageable, Long cursorId) {
        accountService.checkAccountById(accountId);
        Slice<Friendship> friendships = friendshipRepository.findAllFriendshipByAccountSortCreatedAt(accountId, pageable, cursorId);
        Slice<FriendshipCardResponse> result = friendships.map(FriendshipCardResponse::of);

        return SliceResponse.of(result);
    }

    @Transactional
    public ChangeFriendNicknameResponse changeFriendNickname(Long accountId, ChangeFriendNicknameRequest changeFriendNicknameRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account friendAccount = accountService.getAccountById(changeFriendNicknameRequest.friendAccountId());

        Friendship friendship = friendshipRepository.findByAccountSelfAndAccountFriend(loginAccount, friendAccount)
                .orElseThrow(() -> new FriendshipException(StatusCode.NOT_FOUND_FRIENDSHIP));

        String nickname = changeFriendNicknameRequest.nickname();
        if (friendship.getFriendNickname().equals(nickname)) throw new FriendshipException(StatusCode.SAME_NICKNAME_ERROR);

        friendship.changeFriendNickname(nickname);

        return new ChangeFriendNicknameResponse(friendship.getFriendNickname());
    }

    // 친구 닉네임 변경시 친구 닉네임 가져오기 (List)
    public String getFriendNickname(List<Friendship> friendships, Account friendAccount) {
        Optional<Friendship> friendshipOptional = friendships.stream()
                .filter(friendship -> friendship.getAccountFriend().getId().equals(friendAccount.getId()))
                .findFirst();
        return friendshipOptional.map(Friendship::getFriendNickname).orElse(friendAccount.getNickname());
    }

    public String getFriendNicknameByAccountSelf(List<Friendship> friendships, Account account, Account friendAccount) {
        Optional<Friendship> friendshipOptional = friendships.stream()
                .filter(friendship -> friendship.getAccountSelf().getId().equals(account.getId()))
                .findFirst();

        return friendshipOptional.map(Friendship::getFriendNickname).orElse(friendAccount.getNickname());
    }

    // 친구 닉네임 변경시 친구 닉네임 가져오기 (단건 조회)
    public String getFriendNicknameSingle(Account loginAccount, Account friendAccount) {
        Optional<Friendship> friendshipOptional = friendshipRepository
                .findByAccountSelfAndAccountFriendAndIsChangeFriendNickname(loginAccount, friendAccount, true);
        return friendshipOptional.map(Friendship::getFriendNickname).orElse(friendAccount.getNickname());
    }


}
