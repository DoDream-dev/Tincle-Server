package tinqle.tinqleServer.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.room.model.Room;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

    List<Message> findAllByReceiverAndRoomAndIsReadFromReceiverIsFalse(Account receiver, Room room);

    Long countAllByReceiverAndIsReadFromReceiverIsFalse(Account receiver);

    Long countAllByReceiverAndRoomAndIsReadFromReceiverIsFalse(Account receiver, Room room);

    Optional<Message> findTop1ByRoomOrderByIdDesc(Room room);
}
