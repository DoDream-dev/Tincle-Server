package tinqle.tinqleServer.domain.emoticon.repository;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCountVo;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.List;

public interface EmoticonRepositoryCustom {

    List<EmoticonCountVo> countAllEmoticonTypeByFeedAndVisibleIsTrue(Feed feed);
    List<EmoticonCountVo> countAllEmoticonTypeByFeedAndAccountAndVisibleIsTrue(Feed feed, Account account);
}
