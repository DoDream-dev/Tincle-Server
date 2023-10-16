package tinqle.tinqleServer.domain.messageBox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;

public interface MessageBoxRepository extends JpaRepository<MessageBox, Long> {
}
