package tinqle.tinqleServer.domain.friendship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;


public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {

    boolean existsByRequestAccountAndResponseAccountAndRequestStatus(
            Account requestAccount, Account responseAccount, RequestStatus requestStatus);
}
