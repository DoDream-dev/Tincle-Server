package tinqle.tinqleServer.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
