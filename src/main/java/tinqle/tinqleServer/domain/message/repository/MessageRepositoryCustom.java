package tinqle.tinqleServer.domain.message.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.message.model.Message;

public interface MessageRepositoryCustom {

    Slice<Message> findByRoomSortRecently(Long roomId, Pageable pageable, Long cursorId);
}
