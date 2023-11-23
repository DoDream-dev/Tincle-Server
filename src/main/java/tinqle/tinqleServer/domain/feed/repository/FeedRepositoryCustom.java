package tinqle.tinqleServer.domain.feed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.feed.model.Feed;


public interface FeedRepositoryCustom {

    Slice<Feed> findAllByFriendWithMe(Long accountId, Pageable pageable, Long cursorId);
}
