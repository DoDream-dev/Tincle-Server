package tinqle.tinqleServer.domain.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByAccountSelfAndAccountFriend(Account accountSelf, Account accountFriend);
    boolean existsByAccountSelfAndAccountFriend(Account accountSelf, Account accountFriend);
    boolean existsByAccountSelfIdAndAccountFriendId(Long accountSelfId, Long accountFriendId);
}
