package tinqle.tinqleServer.domain.notification.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Status;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

public class NotificationResponseDto {
    public record NotificationResponse(Long notificationId,
                                          String targetEntity,
                                          NotificationType notificationType,
                                          Long redirectTargetId,
                                          String content,
                                          String friendNickname,
                                          String status,
                                          boolean isRead) {
        @Builder
        public NotificationResponse {}

        public static NotificationResponse of(Notification notification, String friendNickname, Status status) {
            return NotificationResponse.builder()
                    .notificationId(notification.getId())
                    .notificationType(notification.getType())
                    .redirectTargetId(notification.getRedirectTargetId())
                    .content(notification.getContent())
                    .friendNickname(friendNickname)
                    .status(status.toString())
                    .isRead(notification.isRead())
                    .build();
        }

        public static NotificationResponse ofSimple(Notification notification) {
            return NotificationResponse.builder()
                    .notificationId(notification.getId())
                    .notificationType(notification.getType())
                    .redirectTargetId(notification.getRedirectTargetId())
                    .content(notification.getContent())
                    .friendNickname(null)
                    .status(null)
                    .isRead(notification.isRead())
                    .build();
        }
    }
}
