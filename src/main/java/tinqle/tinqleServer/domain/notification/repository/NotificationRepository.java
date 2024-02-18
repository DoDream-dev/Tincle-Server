package tinqle.tinqleServer.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    Optional<Notification> findByIdAndAccount(Long notificationId, Account account);
    List<Notification> findAllByAccountAndIsReadAndVisibilityIsTrue(Account account, boolean isRead);
    Long countAllByAccountAndIsReadAndVisibilityIsTrue(Account account, boolean isRead);
    List<Notification> findAllByAccountAndIsClickedAndVisibilityIsTrue(Account account, boolean isClicked);
    List<Notification> findAllByAccountAndTypeAndVisibilityIsTrue(Account account, NotificationType notificationType);
}
