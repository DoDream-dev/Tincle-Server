package tinqle.tinqleServer.domain.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.room.model.Room;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.message.model.QMessage.message;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    @Override
    public Slice<Message> findByRoomSortRecently(Long roomId, Pageable pageable, Long cursorId, boolean isStarter) {
        JPAQuery<Message> query = queryFactory.selectFrom(message)
                .where(message.room.id.eq(roomId),
                        specifyPosition(isStarter)
                        .and(ltCursorId(cursorId)))
                .orderBy(message.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    @Override
    public void deleteAllMessageWhenStarter(Account account, Room room) {
        queryFactory.update(message)
                .where(message.room.eq(room).and(message.isDeletedFromStarter.isFalse()))
                .set(message.isDeletedFromStarter, true)
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void deleteAllMessageWhenFriend(Account account, Room room) {
        queryFactory.update(message)
                .where(message.room.eq(room).and(message.isDeletedFromFriend.isFalse()))
                .set(message.isDeletedFromFriend, true)
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void readAllMessage(Account account, Room room) {
        queryFactory.update(message)
                .where(message.room.eq(room), message.isReadFromReceiver.isFalse(), message.receiver.eq(account))
                .set(message.isReadFromReceiver, true)
                .execute();
//        em.flush();   추후에 사용할 일 없어서 우선 주석처리
//        em.clear();
    }


    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return message.id.lt(cursorId);
    }

    private BooleanExpression specifyPosition(boolean isStarter) {
        if (isStarter) return message.isDeletedFromStarter.isFalse();

        return message.isDeletedFromFriend.isFalse();
    }
}
