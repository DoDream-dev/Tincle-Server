package tinqle.tinqleServer.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.AccountPolicy;

public interface AccountPolicyRepository extends JpaRepository<AccountPolicy, Long> {
}
