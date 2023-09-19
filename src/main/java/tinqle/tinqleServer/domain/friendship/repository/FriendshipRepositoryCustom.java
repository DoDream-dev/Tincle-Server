package tinqle.tinqleServer.domain.friendship.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

public interface FriendshipRepositoryCustom {
    Slice<Friendship> findAllFriendshipByAccountSortCreatedAt(Long accountId, Pageable pageable, Long cursorId);
}
