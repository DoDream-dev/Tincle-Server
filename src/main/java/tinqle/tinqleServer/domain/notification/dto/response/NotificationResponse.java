package tinqle.tinqleServer.domain.notification.dto.response;

import tinqle.tinqleServer.domain.notification.model.NotificationType;

import java.util.List;

public class NotificationResponse {
    public record GetNotificationResponse(Long notificationId,
                                          NotificationType notificationType,
                                          String targetEntity,
                                          Long redirectTargetId,
                                          String title,
                                          String body,
                                          String createdAt,
                                          boolean isRead) {}

    public record GetNotificationListResponse(List<GetNotificationResponse> notifications) {}
}
