package tinqle.tinqleServer.domain.notification.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_account_id")
    private Account sendAccount;
    private String content;
    private String title;
    private boolean isRead;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Long redirectTargetId;

    public void read() {
        this.isRead = true;
    }
}
