package tinqle.tinqleServer.domain.block.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.block.model.Block;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    boolean existsByRequesterAccountAndBlockedAccountAndVisibilityIsTrue(Account account, Account blockedAccount);
    Optional<Block> findByRequesterAccountIdAndBlockedAccountId(Long requesterId, Long blockedAccountId);
}
