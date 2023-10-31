package tinqle.tinqleServer.domain.notification.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.QAccount;
import tinqle.tinqleServer.domain.notification.model.Notification;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.notification.model.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Notification> findByAccountAndSortByLatest(Account account, Pageable pageable, Long cursorId) {
        JPAQuery<Notification> query = queryFactory.selectFrom(notification)
                .join(notification.sendAccount, QAccount.account).fetchJoin()
                .where(notification.account.id.eq(account.getId())
                        .and(notification.isRead.isFalse())
                        .and(ltCursorId(cursorId)))
                .orderBy(notification.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return notification.id.lt(cursorId);
    }
}
