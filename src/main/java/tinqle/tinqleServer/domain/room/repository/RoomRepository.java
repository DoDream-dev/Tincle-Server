package tinqle.tinqleServer.domain.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.room.model.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom{

    Optional<Room> findByStarterAndFriend(Account starter, Account friend);

}
