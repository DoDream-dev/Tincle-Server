package tinqle.tinqleServer.domain.message.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.room.model.Room;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private String content;

    boolean isReadFromReceiver = false;

    boolean isDeletedFromSender = false;

    boolean isDeletedFromReceiver = false;

    public static Message of(Account sender, Account receiver, Room room, String content) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .room(room)
                .content(content)
                .build();

    }

    public boolean isAuthor(Account account) {
        return this.sender.getId().equals(account.getId());
    }

    public void read() {
        this.isReadFromReceiver = true;
    }
}
