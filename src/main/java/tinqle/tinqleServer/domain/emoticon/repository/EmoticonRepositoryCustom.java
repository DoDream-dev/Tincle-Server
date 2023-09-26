package tinqle.tinqleServer.domain.emoticon.repository;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.emoticon.dto.vo.EmoticonCountVo;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.List;

public interface EmoticonRepositoryCustom {

    List<EmoticonCountVo> countAllEmoticonTypeByFeed(Feed feed);
    List<EmoticonCountVo> countAllEmoticonTypeByFeedAndAccount(Feed feed, Account account);
}
