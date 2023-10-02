package tinqle.tinqleServer.domain.emoticon.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.dto.vo.QEmoticonCountVo;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.List;

import static tinqle.tinqleServer.domain.emoticon.model.QEmoticon.emoticon;

@RequiredArgsConstructor
public class EmoticonRepositoryImpl implements EmoticonRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmoticonCountVo> countAllEmoticonTypeByFeedAndVisibleIsTrue(Feed feed) {
        JPAQuery<EmoticonCountVo> query = queryFactory.select(
                new QEmoticonCountVo(emoticon.emoticonType.stringValue(),emoticon.emoticonType.count()))
                .from(emoticon)
                .where(emoticon.feed.id.eq(feed.getId())
                        .and(emoticon.visibility.isTrue()))
                .groupBy(emoticon.emoticonType);
        return query.fetch();
    }

    @Override
    public List<EmoticonCountVo> countAllEmoticonTypeByFeedAndAccountAndVisibleIsTrue(Feed feed, Account account) {
        JPAQuery<EmoticonCountVo> query = queryFactory.select(
                        new QEmoticonCountVo(emoticon.emoticonType.stringValue(),emoticon.emoticonType.count()))
                .from(emoticon)
                .where(emoticon.feed.id.eq(feed.getId())
                        .and(emoticon.visibility.isTrue())
                        .and(emoticon.account.id.eq(account.getId()))
                )
                .groupBy(emoticon.emoticonType);
        return query.fetch();
    }
}
