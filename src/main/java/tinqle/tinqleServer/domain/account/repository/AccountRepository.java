package tinqle.tinqleServer.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
    Optional<Account> findBySocialEmail(String socialEmail);
    boolean existsByCode(String code);
    boolean existsBySocialEmail(String socialEmail);
    Optional<Account> findByCode(String code);
}
