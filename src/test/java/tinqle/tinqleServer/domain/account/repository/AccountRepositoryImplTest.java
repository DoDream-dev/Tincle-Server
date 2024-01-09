package tinqle.tinqleServer.domain.account.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.config.TestQueryDslConfig;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.createDummyAccountExceptId;
import static tinqle.tinqleServer.domain.comment.template.CommentTemplate.createDummyChildCommentExceptId;
import static tinqle.tinqleServer.domain.comment.template.CommentTemplate.createDummyParentCommentExceptId;
import static tinqle.tinqleServer.domain.feed.template.FeedTemplate.createDummyFeedAExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public class AccountRepositoryImplTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void clear() {
        accountRepository.deleteAll();
        feedRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @AfterAll
    public void clearAfterAll() {
        accountRepository.deleteAll();
        feedRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("피드 작성자가 댓글 작성시 자신을 제외한 댓글 단 사람 조회 - 성공")
    public void findParentCommentAuthorByFeedDistinctExceptFeedAuthor_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB));
        accountRepository.saveAll(dummyAccounts);

        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);
        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);

        List<Comment> comments = new ArrayList<>(Arrays.asList(comment, feedAuthorComment));
        commentRepository.saveAll(comments);

        //when
        List<Account> accounts = accountRepository.findCommentAuthorByFeedDistinctExceptFeedAuthor(feed, dummyAccountA);

        //then
        assertThat(accounts.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("피드 작성자가 댓글 작성시 자신을 제외한 댓글/대댓글 단 사람 조회 - 성공")
    public void findAllCommentAuthorByFeedDistinctExceptFeedAuthor_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC));
        accountRepository.saveAll(dummyAccounts);

        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);

        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);
        Comment childComment = createDummyChildCommentExceptId(dummyAccountC, comment);
        Comment childComment_B = createDummyChildCommentExceptId(dummyAccountC, comment);

        List<Comment> comments = new ArrayList<>(Arrays.asList(comment, feedAuthorComment));
        commentRepository.saveAll(comments);
        List<Comment> childComments = new ArrayList<>(Arrays.asList(childComment, childComment_B));
        commentRepository.saveAll(childComments);

        //when
        List<Account> accounts = accountRepository.findCommentAuthorByFeedDistinctExceptFeedAuthor(feed, dummyAccountA);

        //then
        assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("피드 작성자, 부모 댓글 작성자 제외한 대댓글에 참여한 대댓글 작성자 조회 - 성공")
    public void findChildCommentAuthorByParentCommentDistinctExceptAuthors_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        Account dummyAccountD = createDummyAccountExceptId("닉네임2");
        List<Account> dummyAccounts =
                new ArrayList<>(Arrays.asList(dummyAccountA,dummyAccountB,dummyAccountC,dummyAccountD));
        accountRepository.saveAll(dummyAccounts);

        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);

        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);
        Comment childComment = createDummyChildCommentExceptId(dummyAccountC, comment);
        Comment childComment_B = createDummyChildCommentExceptId(dummyAccountD, comment);

        List<Comment> comments = new ArrayList<>(Arrays.asList(comment, feedAuthorComment));
        commentRepository.saveAll(comments);
        List<Comment> childComments = new ArrayList<>(Arrays.asList(childComment, childComment_B));
        commentRepository.saveAll(childComments);
        //when
        List<Account> accounts = accountRepository.findChildCommentAuthorByParentCommentDistinctExceptAuthors(comment, dummyAccountA, dummyAccountB, dummyAccountD);

        //then
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getNickname()).isEqualTo(dummyAccountC.getNickname());
    }
}
