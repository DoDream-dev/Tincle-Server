package tinqle.tinqleServer.domain.room.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    private String sessionId;

    public static Session of(Account account, Room room, String sessionId) {
        return Session.builder()
                .account(account)
                .room(room)
                .sessionId(sessionId)
                .build();
    }

    public void updateSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
