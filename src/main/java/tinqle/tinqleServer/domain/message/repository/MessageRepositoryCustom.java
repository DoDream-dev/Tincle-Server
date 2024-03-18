package tinqle.tinqleServer.domain.message.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.room.model.Room;

public interface MessageRepositoryCustom {

    Slice<Message> findByRoomSortRecently(Long roomId, Pageable pageable, Long cursorId, boolean isStarter);
    void deleteAllMessageWhenStarter(Account account, Room room);
    void deleteAllMessageWhenFriend(Account account, Room room);
}
