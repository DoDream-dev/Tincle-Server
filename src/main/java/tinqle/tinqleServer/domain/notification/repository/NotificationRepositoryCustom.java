package tinqle.tinqleServer.domain.notification.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.notification.model.Notification;

public interface NotificationRepositoryCustom {

    Slice<Notification> findByAccountAndSortByLatest(Account account, Pageable pageable, Long cursorId);
}
