package tinqle.tinqleServer.domain.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tinqle.tinqleServer.domain.feed.model.Feed;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    CREATE_FEED_COMMENT(ReceiverType.AUTHOR, Feed.class);
    private enum ReceiverType {
        AUTHOR,USER
    }
    private final ReceiverType receiverType;
    private final Class<?> redirectTargetClass;
}
