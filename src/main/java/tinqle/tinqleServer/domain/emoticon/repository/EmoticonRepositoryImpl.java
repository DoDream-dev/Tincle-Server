package tinqle.tinqleServer.domain.emoticon.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.emoticon.dto.dao.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.dto.dao.QEmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tinqle.tinqleServer.domain.emoticon.model.QEmoticon.emoticon;

@RequiredArgsConstructor
public class EmoticonRepositoryImpl implements EmoticonRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmoticonCountVo> countEmoticonTypeByFeed(Feed feed) {
        JPAQuery<EmoticonCountVo> query = queryFactory.select(
                new QEmoticonCountVo(emoticon.emoticonType.stringValue(),emoticon.emoticonType.count()))
                .from(emoticon)
                .where(emoticon.feed.id.eq(feed.getId())
                        .and(emoticon.visibility.isTrue()))
                .groupBy(emoticon.emoticonType);
        return query.fetch();
    }

    @Override
    public Map<EmoticonType, Boolean> isCheckedEmoticonByFeedAndAccount(Feed feed, Account account) {
        List<EmoticonType> emoticonTypes = Arrays.asList(EmoticonType.SMILE, EmoticonType.SAD, EmoticonType.HEART, EmoticonType.SURPRISE);

        Map<EmoticonType, Boolean> resultMap = new HashMap<>();

        for (EmoticonType emoticonType : emoticonTypes) {
            boolean isChecked = queryFactory.selectOne()
                    .from(emoticon)
                    .where(
                            emoticon.account.id.eq(account.getId()),
                            emoticon.feed.eq(feed),
                            emoticon.emoticonType.eq(emoticonType),
                            emoticon.visibility.isTrue()
                    )
                    .fetchFirst() != null;

            resultMap.put(emoticonType, isChecked);
        }

        return resultMap;

    }


}
