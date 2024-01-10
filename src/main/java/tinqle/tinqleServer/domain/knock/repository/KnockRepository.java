package tinqle.tinqleServer.domain.knock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.knock.model.Knock;

import java.util.List;

public interface KnockRepository extends JpaRepository<Knock, Long> {
    List<Knock> findAllByAccountAndVisibilityIsTrue(Account account);

    boolean existsByAccountAndSendAccountAndVisibilityIsTrue(Account account, Account sendAccount);
}
