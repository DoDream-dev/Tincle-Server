package tinqle.tinqleServer.domain.notification.dto;

public class FcmDto {

    public record FcmMessage(
            Boolean validate_only,
            Message message
    ) {}
    public record Message(
            Android android,
            Apns apns,
            String token
    ) {}

    public record Android(
            String priority,
            Notification notification,
            Data data

    ) {}

    public record Notification(
            String sound,
            String title,
            String body
    ) {}

    public record Data(
            String redirectTargetId,
            String type
    ) {}

    public record Apns(
            Payload payload
    ) {}

    public record Payload(
            Aps aps,
            String redirectTargetId,
            String type,
            String title,
            String body
    ) {}

    public record Aps(
            String sound,
            Long contentAvailable,
            Alert alert
    ) {}

    public record Alert(
            String title,
            String body,
            String actionLocKey
    ) {}


//    public record FcmMessage(
//            Boolean validate_only,
//            Message message
//    ) {}
//
//    public record Message(
//            String token,
//            Notification data
//    ) {}
//
//    public record Notification(
//            String title,
//            String body,
//            String redirectTargetId,
//            String type
//    ) {}
//
//    public record PushMessage(
//            Long receiverId,
//            String title,
//            String body
//    ) {}
}
