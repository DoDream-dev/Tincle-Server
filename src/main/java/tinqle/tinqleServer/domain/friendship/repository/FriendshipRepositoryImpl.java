package tinqle.tinqleServer.domain.friendship.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.QAccount;
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
                        .and(ltCursorId(cursorId))
                        .and(friendship.visibility.isTrue()))
                .orderBy(friendship.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    @Override
    public List<Friendship> findAllByAccountSelfAndIsChangeFriendNickname(Long accountId, boolean isChangeFriendNickname) {
        JPAQuery<Friendship> query = queryFactory.selectFrom(friendship)
                .join(friendship.accountFriend, account).fetchJoin()
                .where(friendship.accountSelf.id.eq(accountId)
                        .and(friendship.isChangeFriendNickname.eq(isChangeFriendNickname))
                        .and(friendship.visibility.isTrue()));

        return query.fetch();
    }

    @Override
    public List<Friendship> findAllByAccountFriendAndIsChangeFriendNickname(Account account, boolean isChangeFriendNickname) {
        JPAQuery<Friendship> query = queryFactory.selectFrom(friendship)
                .join(friendship.accountSelf, QAccount.account).fetchJoin()
                .where(friendship.isChangeFriendNickname.eq(isChangeFriendNickname)
                        .and(friendship.accountFriend.id.eq(account.getId()))
                        .and(friendship.isChangeFriendNickname.isTrue())
                        .and(friendship.visibility.isTrue()));

        return query.fetch();
    }


    @Override
    public Slice<Friendship> findContainFriendNicknameAndIdNotEqual(Long accountId, Pageable pageable, Long cursorId, String keyword) {
        JPAQuery<Friendship> query = queryFactory.select(friendship).distinct()
                .from(friendship)
                .join(friendship.accountFriend, account).fetchJoin()
                .where(friendship.accountSelf.id.eq(accountId)
                        .and(friendship.isChangeFriendNickname.isTrue()
                                .and(friendship.accountFriend.code.contains(keyword)
                                        .or(friendship.friendNickname.contains(keyword)))
                                .or(friendship.isChangeFriendNickname.isFalse()
                                        .and(friendship.accountFriend.code.contains(keyword)
                                                .or(friendship.accountFriend.nickname.contains(keyword)))
                                )
                        )
                        .and(friendship.accountFriend.code.notIn(keyword))
                        .and(ltCursorId(cursorId)))
                .orderBy(friendship.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    /**
     * BooleanExpression
     **/
    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return friendship.id.lt(cursorId);
    }
}
