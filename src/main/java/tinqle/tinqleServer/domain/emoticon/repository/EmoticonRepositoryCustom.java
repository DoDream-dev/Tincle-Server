package tinqle.tinqleServer.domain.emoticon.repository;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.emoticon.dto.dao.EmoticonCountVo;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.List;
import java.util.Map;

public interface EmoticonRepositoryCustom {

    List<EmoticonCountVo> countEmoticonTypeByFeed(Feed feed);
    Map<EmoticonType, Boolean> isCheckedEmoticonByFeedAndAccount(Feed feed, Account account);
}
