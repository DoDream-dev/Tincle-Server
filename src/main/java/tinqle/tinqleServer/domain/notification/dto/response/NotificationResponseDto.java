package tinqle.tinqleServer.domain.notification.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

public class NotificationResponseDto {
    public record NotificationResponse(Long notificationId,
                                          String targetEntity,
                                          NotificationType notificationType,
                                          Long redirectTargetId,
                                          String content,
                                          boolean isRead) {
        @Builder
        public NotificationResponse {}

        public static NotificationResponse of(Notification notification) {
            return NotificationResponse.builder()
                    .notificationId(notification.getId())
                    .notificationType(notification.getType())
                    .redirectTargetId(notification.getRedirectTargetId())
                    .content(notification.getContent())
                    .isRead(notification.isRead())
                    .build();
        }
    }
}
