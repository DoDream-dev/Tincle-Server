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
public class Friendship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_self_id")
    private Account accountSelf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_friend_id")
    private Account accountFriend;
    private String friendNickname;
    private boolean isChangeFriendNickname = false;
}
