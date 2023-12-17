package tinqle.tinqleServer.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.util.CustomSliceExecutionUtil;

import static tinqle.tinqleServer.domain.account.model.QAccount.account;
import static tinqle.tinqleServer.domain.comment.model.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Comment> findAllByFeed(Long feedId, Pageable pageable, Long cursorId) {
        JPAQuery<Comment> query = queryFactory.selectFrom(comment)
                .join(comment.account, account).fetchJoin()
                .where(comment.feed.id.eq(feedId)
                        .and(comment.parent.isNull())
                        .and(gtCursorId(cursorId)))
                .orderBy(comment.id.asc())
                .limit(CustomSliceExecutionUtil.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtil.getSlice(query.fetch(), pageable.getPageSize());
    }

    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return comment.id.gt(cursorId);
    }
}
