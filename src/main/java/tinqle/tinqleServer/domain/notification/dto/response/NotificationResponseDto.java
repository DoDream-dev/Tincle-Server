package tinqle.tinqleServer.domain.notification.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

public class NotificationResponseDto {
    public record NotificationResponse(Long notificationId,
                                          String targetEntity,
                                          NotificationType notificationType,
                                          Long redirectTargetId,
                                          String content,
                                          Long accountId,
                                          String friendNickname,
                                          String status,
                                          String profileImageUrl,
                                          boolean isRead,
                                          boolean isClicked) {
        @Builder
        public NotificationResponse {}

        public static NotificationResponse of(Notification notification, String friendNickname, Account sender) {
            return NotificationResponse.builder()
                    .notificationId(notification.getId())
                    .notificationType(notification.getType())
                    .redirectTargetId(notification.getRedirectTargetId())
                    .content(notification.getContent())
                    .accountId(sender.getId())
                    .friendNickname(friendNickname)
                    .status(sender.getStatus().toString())
                    .profileImageUrl(sender.getProfileImageUrl())
                    .isRead(notification.isRead())
                    .isClicked(notification.isClicked())
                    .build();
        }
    }
    public record UnReadCountNotificationResponse(Long count) {}
    public record DeleteNotificationResponse(boolean isDelete) {}
    public record ClickNotificationResponse(boolean result) {}
}
