package tinqle.tinqleServer.domain.account.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tinqle.tinqleServer.domain.account.template.AccountTemplate.*;
import static tinqle.tinqleServer.domain.comment.template.CommentTemplate.createDummyChildCommentExceptId;
import static tinqle.tinqleServer.domain.comment.template.CommentTemplate.createDummyParentCommentExceptId;
import static tinqle.tinqleServer.domain.feed.template.FeedTemplate.createDummyFeedAExceptId;

@DataJpaTest
@ActiveProfiles({"test"})
@Import(TestQueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {
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

    @Test
    @DisplayName("account 저장 - 성공")
    public void accountSave_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        Account dummyAccountB = createDummyAccountB_ExceptId();
        accountRepository.save(dummyAccountA);
        accountRepository.save(dummyAccountB);

        //when
        List<Account> accounts = accountRepository.findAll();

        //then
        assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("socialEmail로 찾기 - 성공")
    public void findBySocialEmail_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        Account account = accountRepository.findBySocialEmail(dummyAccountA.getSocialEmail()).orElseThrow(() -> new IllegalArgumentException("에러"));

        //then
        assertThat(account.getSocialEmail()).isEqualTo(dummyAccountA.getSocialEmail());
        assertThat(account.getAccountStatus()).isEqualTo(dummyAccountA.getAccountStatus());
        assertThat(account.getCode()).isEqualTo(dummyAccountA.getCode());
    }

    @Test
    @DisplayName("해당 코드가 있는지 검색 - 성공")
    public void existsByCode_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        boolean exists = accountRepository.existsByCode(dummyAccountA.getCode());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("해당 socialEmail이 있는지 검색 - 성공")
    public void existsBySocialEmail_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);

        //when
        boolean exists = accountRepository.existsBySocialEmail(dummyAccountA.getSocialEmail());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("피드 작성자가 댓글 작성시 자신을 제외한 댓글 단 사람 조회 - 성공")
    public void findParentCommentAuthorByFeedDistinctExceptFeedAuthor_success() throws Exception {
        //given
        Account dummyAccountA = createDummyAccountA_ExceptId();
        accountRepository.save(dummyAccountA);
        Account dummyAccountB = createDummyAccountB_ExceptId();
        accountRepository.save(dummyAccountB);
        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);
        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        commentRepository.save(comment);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);
        commentRepository.save(feedAuthorComment);

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
        accountRepository.save(dummyAccountA);
        Account dummyAccountB = createDummyAccountB_ExceptId();
        accountRepository.save(dummyAccountB);
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        accountRepository.save(dummyAccountC);
        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);
        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        commentRepository.save(comment);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);
        commentRepository.save(feedAuthorComment);
        Comment childComment = createDummyChildCommentExceptId(dummyAccountC, comment);
        commentRepository.save(childComment);
        Comment childComment_B = createDummyChildCommentExceptId(dummyAccountC, comment);
        commentRepository.save(childComment_B);

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
        accountRepository.save(dummyAccountA);
        Account dummyAccountB = createDummyAccountB_ExceptId();
        accountRepository.save(dummyAccountB);
        Account dummyAccountC = createDummyAccountExceptId("닉네임");
        accountRepository.save(dummyAccountC);
        Account dummyAccountD = createDummyAccountExceptId("닉네임2");
        accountRepository.save(dummyAccountD);
        Feed feed = createDummyFeedAExceptId(dummyAccountA);
        feedRepository.save(feed);
        Comment comment = createDummyParentCommentExceptId(dummyAccountB, feed);
        commentRepository.save(comment);
        Comment feedAuthorComment = createDummyParentCommentExceptId(dummyAccountA, feed);
        commentRepository.save(feedAuthorComment);
        Comment childComment = createDummyChildCommentExceptId(dummyAccountC, comment);
        commentRepository.save(childComment);
        Comment childComment_B = createDummyChildCommentExceptId(dummyAccountD, comment);
        commentRepository.save(childComment_B);
        //when
        List<Account> accounts = accountRepository.findChildCommentAuthorByParentCommentDistinctExceptAuthors(comment, dummyAccountA, dummyAccountB, dummyAccountD);

        //then
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getNickname()).isEqualTo(dummyAccountC.getNickname());
    }
}
