package tinqle.tinqleServer.domain.notification.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.notification.model.Notification;

public class NotificationTemplate {

    private static final String CONTENT = "알림 내용";
    private static final String TITLE = "알림 제목";

    private static Notification createNotificationExceptId(
            Account account, Account sendAccount, String content, String title, boolean isRead) {
        return Notification.builder()
                .account(account)
                .sendAccount(sendAccount)
                .content(content)
                .title(title)
                .isRead(isRead).build();
    }

    public static Notification createDummyNotificationExceptId(Account account, Account sendAccount, String content, boolean isRead) {
        return createNotificationExceptId(account, sendAccount, content, TITLE, isRead);
    }
}
