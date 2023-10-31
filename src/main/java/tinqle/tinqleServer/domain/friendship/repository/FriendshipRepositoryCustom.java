package tinqle.tinqleServer.domain.friendship.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import java.util.List;

public interface FriendshipRepositoryCustom {
    Slice<Friendship> findAllFriendshipByAccountSortCreatedAt(Long accountId, Pageable pageable, Long cursorId);
    List<Friendship> findAllByAccountSelfAndIsChangeFriendNickname(Long accountId, boolean isChangeFriendNickname);
    List<Friendship> findAllByAccountFriendAndIsChangeFriendNickname(Account account, boolean changeFriendNickname);

}
