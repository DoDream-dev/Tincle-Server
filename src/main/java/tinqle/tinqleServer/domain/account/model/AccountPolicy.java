package tinqle.tinqleServer.domain.account.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.policy.model.Policy;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_policy_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    private boolean isChecked;

    // 회원(1) - 정책(다)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
