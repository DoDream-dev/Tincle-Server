package tinqle.tinqleServer.domain.friendship.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendshipRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_account_id")
    private Account requestAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_account_id")
    private Account responseAccount;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    private String message;

    public void approve() {
        this.requestStatus = RequestStatus.APPROVE;
    }
    public void reject() {
        this.requestStatus = RequestStatus.REJECT;
    }

}
