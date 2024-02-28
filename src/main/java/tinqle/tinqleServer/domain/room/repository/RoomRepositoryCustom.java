package tinqle.tinqleServer.domain.room.repository;

import tinqle.tinqleServer.domain.room.model.Room;

import java.util.List;

public interface RoomRepositoryCustom {

    List<Room> findAllByAccountAndIsNotDeletedSortRecently(Long accountId);
}
