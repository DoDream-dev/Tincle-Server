package tinqle.tinqleServer.domain.room.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import tinqle.tinqleServer.domain.room.model.Room;

import java.util.List;

import static tinqle.tinqleServer.domain.room.model.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Room> findAllByAccountAndIsNotDeletedSortRecently(Long accountId) {

        return queryFactory.selectFrom(room)
                .join(room.starter).fetchJoin()
                .join(room.friend).fetchJoin()
                .where(room.visibility.isTrue()
                        .and((room.starter.id.eq(accountId).and(room.isDeletedFromStarter.isFalse()))
                                .or(room.friend.id.eq(accountId).and(room.isDeletedFromFriend.isFalse()))
                        )
                )
                .orderBy(room.modifiedAt.desc()).fetch();
    }
}
