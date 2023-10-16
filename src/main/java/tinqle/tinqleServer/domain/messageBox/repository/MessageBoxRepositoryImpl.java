package tinqle.tinqleServer.domain.messageBox.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.messageBox.model.QMessageBox.messageBox;

@RequiredArgsConstructor
public class MessageBoxRepositoryImpl implements MessageBoxRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<MessageBox> findAllByAccountCustom(Account account, Pageable pageable, Long cursorId) {
        JPAQuery<MessageBox> query = queryFactory.selectFrom(messageBox)
                .where(messageBox.visibility.isTrue()
                        .and(messageBox.receiveAccount.id.eq(account.getId()))
                        .and(ltCursorId(cursorId)))
                .orderBy(messageBox.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return messageBox.id.lt(cursorId);
    }
}
