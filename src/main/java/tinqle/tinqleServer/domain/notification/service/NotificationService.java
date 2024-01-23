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
import tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.ClickNotificationResponse;
import tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.DeleteNotificationResponse;
import tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.NotificationResponse;
import tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.UnReadCountNotificationResponse;
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
        Notification notification = Notification.builder()
                .account(receiver)
                .sendAccount(params.sender())
                .title(params.title())
                .content(params.content())
                .isRead(false)
                .type(params.type())
                .redirectTargetId(params.redirectTargetId())
                .isClicked(false)
                .build();

        notificationRepository.save(notification);

        if (!receiver.isReceivedPushNotification()) {
            return;
        }

        fcmService.sendPushMessage(receiver.getFcmToken(), params);
    }

    @Transactional
    public SliceResponse<NotificationResponse> getMyNotifications(Long accountId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Slice<Notification> notifications = notificationRepository.findByAccountAndSortByLatest(loginAccount, pageable, cursorId);
        List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);


        Slice<NotificationResponse> result = notifications.map(notification ->
                NotificationResponse.of(notification, friendshipService
                        .getFriendNickname(friendships, notification.getSendAccount()), notification.getSendAccount()));
        readAllNotification(loginAccount);
        return SliceResponse.of(result);
    }

    @Transactional
    public DeleteNotificationResponse softDeleteNotification(Long accountId, Long notificationId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Notification notification = getNotificationByIdAndAccount(notificationId, loginAccount);

        notification.click();
        notification.softDelete();

        return new DeleteNotificationResponse(notification.isVisibility());
    }

    public UnReadCountNotificationResponse countNotReadNotifications(Long accountId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Long count = notificationRepository.countAllByAccountAndIsReadAndVisibilityIsTrue(loginAccount, false);

        return new UnReadCountNotificationResponse(count);
    }

    @Transactional
    public ClickNotificationResponse clickNotification(Long accountId, Long notificationId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Notification notification = getNotificationByIdAndAccount(notificationId, loginAccount);
        notification.click();

        return new ClickNotificationResponse(notification.isClicked());
    }

    @Transactional(readOnly = true)
    public Notification getNotificationByIdAndAccount(Long notificationId, Account account) {
        return notificationRepository.findByIdAndAccount(notificationId, account)
                .orElseThrow(() -> new NotificationException(StatusCode.NOT_FOUND_NOTIFICATION));
    }

    @Transactional
    public void readAllNotification(Account account) {
        // 읽지 않고 삭제하지 않은 알림들
        List<Notification> notifications = notificationRepository.findAllByAccountAndIsReadAndVisibilityIsTrue(account, false);

        notifications.forEach(Notification::read);
    }

    @Transactional
    public ClickNotificationResponse clickAllNotification(Long accountId) {
        Account loginAccount = accountService.getAccountById(accountId);
        List<Notification> notifications = notificationRepository.
                findAllByAccountAndIsClickedAndVisibilityIsTrue(loginAccount, false);
        notifications.forEach(Notification::click);

        return new ClickNotificationResponse(true);
    }
}
