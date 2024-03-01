package tinqle.tinqleServer.domain.message.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.message.model.QMessage.message;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Message> findByRoomSortRecently(Long roomId, Pageable pageable, Long cursorId) {
        JPAQuery<Message> query = queryFactory.selectFrom(message)
                .where(message.room.id.eq(roomId)
                        .and(ltCursorId(cursorId)))
                .orderBy(message.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }


    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return message.id.lt(cursorId);
    }
}
