package tinqle.tinqleServer.domain.feed.model;

import jakarta.persistence.*;
import lombok.*;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(length = 500)
    private String content;
    private boolean isKnock;
    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emoticon> emoticonList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> feedImageList = new ArrayList<>();

    public void addFeedImage(FeedImage feedImage) {
        if (feedImageList == null) feedImageList = new ArrayList<>();
        feedImageList.add(feedImage);
    }

    public void updateFeed(String updateContent, List<FeedImage> updateFeedImages) {
        this.content = updateContent;
        this.feedImageList.clear();
        this.feedImageList.addAll(updateFeedImages);
    }
}
