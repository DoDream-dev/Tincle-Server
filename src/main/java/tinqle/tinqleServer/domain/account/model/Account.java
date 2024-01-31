package tinqle.tinqleServer.domain.account.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.block.model.Block;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.knock.model.Knock;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;
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
    @Enumerated(EnumType.STRING)
    private Status status;
    private String password;
    private String socialEmail;
    @Column(unique = true)
    private String code;
    @Size(max = 10)
    private String nickname;
    private String profileImageUrl;
    private String fcmToken;
    private String providerRefreshToken;
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
    @OneToMany(mappedBy = "sendAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> targetAccounts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountPolicy> accountPolicyList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "requestAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> requestAccountList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "responseAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipRequest> responseAccountList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sendAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageBox> sendAccountList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiveAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageBox> receiveAccountList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Knock> knockTargetAccounts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sendAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Knock> knockSendAccounts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "requesterAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> requesterAccounts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "blockedAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Block> blockedAccounts = new ArrayList<>();
    public void addAccountPolicy(AccountPolicy accountPolicy) {
        if (accountPolicyList == null) accountPolicyList = new ArrayList<>();
        accountPolicyList.add(accountPolicy);
    }


    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updateFcmToken(String newFcmToken) {
        this.fcmToken = newFcmToken;
    }

    public void updateProviderRefreshToken(String refreshToken) {
        this.providerRefreshToken = refreshToken;
    }
    public void updatePushNotificationStatus(boolean isReceivedPushNotification) {
        this.isReceivedPushNotification = isReceivedPushNotification;
    }

    public void deleteFcmToken() {
        this.fcmToken = null;
    }
}
