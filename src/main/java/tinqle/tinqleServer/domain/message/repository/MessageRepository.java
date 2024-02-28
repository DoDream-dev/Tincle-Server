package tinqle.tinqleServer.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.message.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
