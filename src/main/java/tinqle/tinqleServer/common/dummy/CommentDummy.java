package tinqle.tinqleServer.common.dummy;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.util.List;

@Component("commentDummy")
@DependsOn("feedDummy")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentDummy {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final AccountRepository accountRepository;


    @PostConstruct
    public void init() {
        if (commentRepository.count() > 0) {
            log.info("[commentDummy] 더미 댓글이 이미 존재합니다.");
        } else {
            createComments();
            log.info("[commentDummy] 더미 댓글 생성완료");
        }
    }

    private void createComments() {
        List<Feed> feeds = feedRepository.findAll();
        for (Feed feed : feeds) {
            List<Account> accounts = accountRepository.findAll();

            for (int i = 0; i < 10; i++) {// 유저 10명
                Comment parent = commentRepository.save(Comment.builder()
                        .account(accounts.get(i))
                        .feed(feed)
                        .content("댓글" + feed.getId() * (i + 1))
                        .build());

                for (int j = 0; j < 5; j++) {
                    commentRepository.save(Comment.builder()
                            .account(accounts.get(j))
                            .feed(feed)
                            .parent(parent)
                            .content(parent.getId() + "의 대댓글")
                            .build());
                }
            }
        }
    }
}
