package tinqle.tinqleServer.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.NotificationResponse;
import tinqle.tinqleServer.domain.notification.exception.NotificationException;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.domain.notification.repository.NotificationRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final FcmService fcmService;
    private final AccountService accountService;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void pushMessage(NotifyParams params) {
        Account receiver = accountService.getAccountById(params.receiver().getId());

        fcmService.sendPushMessage(receiver.getFcmToken(), params);
        Notification notification = Notification.builder()
                .account(receiver)
                .sendAccount(params.sender())
                .title(params.title())
                .content(params.content())
                .isRead(false)
                .type(params.type())
                .redirectTargetId(params.redirectTargetId())
                .build();

        notificationRepository.save(notification);
    }

    public SliceResponse<NotificationResponse> getMyNotifications(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Slice<Notification> notifications = notificationRepository.findByAccountAndSortByLatest(loginAccount, pageable, cursorId);
        List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);


        Slice<NotificationResponse> result = notifications.map(notification ->
                NotificationResponse.of(notification, friendshipService
                        .getFriendNickname(friendships, notification.getSendAccount()), notification.getSendAccount().getStatus()));
        return SliceResponse.of(result);
    }

    @Transactional
    public NotificationResponse readNotification(Long accountId, Long notificationId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Notification notification = notificationRepository.findByIdAndAccount(notificationId, loginAccount)
                .orElseThrow(() -> new NotificationException(StatusCode.NOT_FOUND_NOTIFICATION));

        notification.read();

        return NotificationResponse.ofSimple(notification);
    }
}
