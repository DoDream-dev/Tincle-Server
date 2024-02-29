package tinqle.tinqleServer.domain.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.room.model.Room;
import tinqle.tinqleServer.domain.room.model.Session;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByAccountAndRoom(Account account, Room room);
    Optional<Session> findBySessionId(String sessionId);
}
