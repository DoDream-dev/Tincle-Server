package tinqle.tinqleServer.domain.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.dto.request.FriendshipRequestDto.RequestFriendship;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.FriendshipReqeustResponse;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.FriendshipRequestMessageResponse;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.ResponseFriendship;
import tinqle.tinqleServer.domain.friendship.exception.FriendshipException;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRequestRepository;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendshipRequestService {

    private final AccountService accountService;
    private final FriendshipRequestRepository requestRepository;
    private final NotificationService notificationService;
    private final FriendshipRepository friendshipRepository;


    @Transactional
    public ResponseFriendship friendshipRequest(Long accountId, RequestFriendship requestFriendship) {
        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(requestFriendship.accountTargetId());
        boolean requestExists = requestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(
                loginAccount, targetAccount, RequestStatus.WAITING);
        if (requestExists) throw new FriendshipException(StatusCode.DUPLICATE_FRIENDSHIP_REQUEST);

        isCheckAlreadyFriend(loginAccount, targetAccount);

        FriendshipRequest friendshipRequest = FriendshipRequest.builder()
                .requestAccount(loginAccount)
                .responseAccount(targetAccount)
                .requestStatus(RequestStatus.WAITING)
                .message(requestFriendship.message()).build();

        requestRepository.save(friendshipRequest);

        notificationService.pushMessage(NotifyParams.ofCreateFriendshipRequest(friendshipRequest));

        return new ResponseFriendship(friendshipRequest.getId());
    }

    private void isCheckAlreadyFriend(Account loginAccount, Account targetAccount) {
        boolean friendExists = friendshipRepository.existsByAccountSelfAndAccountFriend(loginAccount, targetAccount);
        if (friendExists) throw new FriendshipException(StatusCode.ALREADY_FRIENDSHIP);
    }

    @Transactional
    public FriendshipReqeustResponse approveFriendshipRequest(Long accountId, Long friendshipRequestId) {
        Account loginAccount = accountService.getAccountById(accountId);
        FriendshipRequest friendshipRequest = getFriendshipRequestById(friendshipRequestId);
        Account requestAccount = friendshipRequest.getRequestAccount();

        checkIsCorrectRequest(loginAccount, friendshipRequest);

        friendshipRequest.approve();
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

//        2차 범위
//        notificationService.pushMessage(NotificationDto.NotifyParams.ofApproveFriendshipRequest(friendshipRequest));

        boolean result = friendshipRepository.existsByAccountSelfAndAccountFriend(loginAccount, requestAccount);

        return new FriendshipReqeustResponse(result);
    }

    @Transactional
    public FriendshipReqeustResponse rejectFriendRequest(Long accountId, Long friendshipRequestId) {
        Account loginAccount = accountService.getAccountById(accountId);
        FriendshipRequest friendshipRequest = getFriendshipRequestById(friendshipRequestId);

        checkIsCorrectRequest(loginAccount, friendshipRequest);
        friendshipRequest.reject();

        boolean exists = friendshipRepository.
                existsByAccountSelfIdAndAccountFriendId(accountId, friendshipRequest.getRequestAccount().getId());
        return FriendshipReqeustResponse.of(exists);
    }

    private static void checkIsCorrectRequest(Account loginAccount, FriendshipRequest friendshipRequest) {
        if (!friendshipRequest.getResponseAccount().getId().equals(loginAccount.getId())
                || !friendshipRequest.getRequestStatus().equals(RequestStatus.WAITING))
            throw new FriendshipException(StatusCode.IS_NOT_CORRECT_FRIENDSHIP_REQUEST);
    }

    private FriendshipRequest getFriendshipRequestById(Long friendshipRequestId) {
        return requestRepository.findById(friendshipRequestId)
                .orElseThrow(() -> new FriendshipException(StatusCode.NOT_FOUND_FRIENDSHIP_REQUEST));
    }

    public FriendshipRequestMessageResponse getMessage(Long accountId, Long friendshipRequestId) {
        Account loginAccount = accountService.getAccountById(accountId);
        FriendshipRequest friendshipRequest = getFriendshipRequestById(friendshipRequestId);
        checkIsCorrectRequest(loginAccount, friendshipRequest);

        return new FriendshipRequestMessageResponse(friendshipRequest.getMessage());
    }
}
