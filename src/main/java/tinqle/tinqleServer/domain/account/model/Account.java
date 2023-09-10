package tinqle.tinqleServer.domain.account.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.notification.model.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String password;
    private String socialEmail;
    @Column(unique = true)
    private String code;
    private String nickname;
    private Status status;
    private String fcmToken;
    private boolean isReceivedPushNotification;
    private LocalDateTime lastLoginAt;

    /**
     * 친구 관계
     */
    @Builder.Default
    @OneToMany(mappedBy = "accountSelf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> accountSelfList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "accountFriend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> accountFriendList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feedList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emoticon> emoticonList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountPolicy> accountPolicyList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "requestAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> requestAccountList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "responseAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> responseAccountList = new ArrayList<>();

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }
    public void updateFcmToken(String newFcmToken) {
        this.fcmToken = newFcmToken;
    }
    public void deleteFcmToken() {
        this.fcmToken = null;
    }
}
