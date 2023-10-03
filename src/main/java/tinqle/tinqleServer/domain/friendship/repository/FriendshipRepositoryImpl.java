package tinqle.tinqleServer.domain.friendship.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import java.util.List;

import static tinqle.tinqleServer.domain.account.model.QAccount.account;
import static tinqle.tinqleServer.domain.friendship.model.QFriendship.friendship;

@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Friendship> findAllFriendshipByAccountSortCreatedAt(Long accountId, Pageable pageable, Long cursorId) {
        JPAQuery<Friendship> query = queryFactory.selectFrom(friendship)
                .join(friendship.accountFriend, account).fetchJoin()
                .where(friendship.accountSelf.id.eq(accountId)
                        .and(ltCursorId(cursorId)))
                .orderBy(friendship.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    @Override
    public List<Friendship> findAllByAccountSelfAndIsChangeFriendNickname(Long accountId, boolean isChangeFriendNickname) {
        JPAQuery<Friendship> query = queryFactory.selectFrom(friendship)
                .join(friendship.accountFriend, account).fetchJoin()
                .where(friendship.accountSelf.id.eq(accountId)
                        .and(friendship.isChangeFriendNickname.eq(isChangeFriendNickname)));

        return query.fetch();
    }


    /**
     * BooleanExpression
     **/
    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return friendship.id.lt(cursorId);
    }
}
