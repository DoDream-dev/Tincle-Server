package tinqle.tinqleServer.domain.feed.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.account.model.QAccount.account;
import static tinqle.tinqleServer.domain.feed.model.QFeed.feed;
import static tinqle.tinqleServer.domain.feed.model.QFeedImage.feedImage;
import static tinqle.tinqleServer.domain.friendship.model.QFriendship.friendship;

@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Feed> findAllByFriendWithMe(Long accountId, Pageable pageable, Long cursorId) {
        JPAQuery<Feed> query = queryFactory.selectFrom(feed)
                .join(feed.account, account).fetchJoin()
                .join(feed.feedImageList, feedImage).fetchJoin()
                .where(feed.visibility.isTrue()
                        .and(feed.account.id.in(JPAExpressions
                                .select(friendship.accountFriend.id)
                                .from(friendship)
                                .where(friendship.accountSelf.id.eq(accountId))).or(feed.account.id.eq(accountId)))
                        .and(ltCursorId(cursorId)))
                .orderBy(feed.id.desc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }


    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return feed.id.lt(cursorId);
    }
}
