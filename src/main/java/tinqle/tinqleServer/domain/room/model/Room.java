package tinqle.tinqleServer.domain.room.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.message.model.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starter_id")
    private Account starter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Account friend;

    boolean isDeletedFromStarter = false;
    boolean isDeletedFromFriend = false;
    LocalDateTime lastMessageSendAt;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public static Room of(Account starter, Account friend) {
        return Room.builder()
                .starter(starter)
                .friend(friend)
                .lastMessageSendAt(LocalDateTime.now())
                .build();
    }

    public void activateRoom() {
        this.isDeletedFromStarter = false;
        this.isDeletedFromFriend = false;
        this.lastMessageSendAt = LocalDateTime.now();
    }

    public void quit(Account account) {
        if (this.getStarter().getId().equals(account.getId()))
            this.isDeletedFromStarter = true;
        else this.isDeletedFromFriend = true;
    }

    public boolean isStarter(Account account) {
        return this.getStarter().getId().equals(account.getId());
    }
}
