package tinqle.tinqleServer.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.comment.dto.request.CommentRequestDto.CreateCommentRequest;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.ChildCommentCard;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CommentCardResponse;
import tinqle.tinqleServer.domain.comment.exception.CommentException;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final AccountService accountService;
    private final FeedService feedService;
    private final FriendshipService friendshipService;
    private final NotificationService notificationService;
    private final FriendshipRepository friendshipRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;

    //피드별 댓글 조회
    public SliceResponse<CommentCardResponse> getCommentsByFeed(Long accountId, Long feedId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        feedService.getFeedById(feedId);

        Slice<Comment> comments = commentRepository.findAllByFeedAndVisibleIsTrue(feedId, pageable, cursorId);
        List<Friendship> friendships = friendshipRepository
                .findAllByAccountSelfAndIsChangeFriendNickname(loginAccount.getId(), true);

        Slice<CommentCardResponse> result = comments.map(comment -> CommentCardResponse.of(comment, friendshipService.getFriendNickname(friendships, comment.getAccount()),
                isCommentAuthor(loginAccount, comment), getChildComment(loginAccount, comment, friendships)));

        return SliceResponse.of(result);
    }

    //부모 댓글 생성
    @Transactional
    public CommentCardResponse createParentComment(Long accountId, Long feedId, CreateCommentRequest createCommentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);

        Comment parentComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(null)
                .content(createCommentRequest.content())
                .build();
        commentRepository.save(parentComment);

        boolean equals = checkIsSameAccount(feed.getAccount(), loginAccount);
        // 알림 기능
        if (!equals) {
            String friendNickname = friendshipService.getFriendNicknameSingle(feed.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateCommentOnMyFeed(friendNickname, feed));
        }
        else {
            List<Account> targetAccounts = accountRepository.findParentCommentAuthorByFeedDistinctExceptFeedAuthor(feed, feed.getAccount());
            List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);
            targetAccounts.forEach(
                    targetAccount -> notificationService.pushMessage(NotifyParams.ofCreateCommentAuthorIsFeedAuthor(
                            targetAccount, friendshipService.getFriendNicknameByAccountSelf(friendships, targetAccount, loginAccount), feed)));
        }
        return CommentCardResponse.of(parentComment, loginAccount.getNickname(), true, Collections.emptyList());
    }

    //대댓글 생성
    @Transactional
    public ChildCommentCard createChildComment(Long accountId, Long feedId, Long parentCommentId, CreateCommentRequest createCommentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);
        Comment parentComment = getCommentById(parentCommentId);

        Comment childComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(parentComment)
                .content(createCommentRequest.content())
                .build();
        commentRepository.save(childComment);

        //대댓글 참여한 모두에게 알림(본인 및 피드 작성자 제외)
        List<Account> targetAccounts = accountRepository.findChildCommentAuthorByParentCommentDistinctExceptAuthors(
                parentComment, feed.getAccount(), parentComment.getAccount(), loginAccount);
        List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);
        targetAccounts.forEach(
                targetAccount -> notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnParentComment(
                        targetAccount, friendshipService.getFriendNicknameByAccountSelf(friendships, targetAccount, loginAccount), feed)));

        if (!isFeedAuthor(loginAccount,feed)) {
            String friendNickname = friendshipService.getFriendNicknameSingle(feed.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnMyFeed(friendNickname, feed));
        }
        if (!isCommentAuthor(loginAccount, parentComment) && isDifferentAuthorByFeedAndComment(feed, parentComment)) {
            String friendNickname = friendshipService.getFriendNicknameSingle(feed.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnMyParentComment(friendNickname, parentComment));
        }

        return ChildCommentCard.of(parentComment, childComment, loginAccount.getNickname(), true);
    }

    private static boolean isDifferentAuthorByFeedAndComment(Feed feed, Comment parentComment) {
        return !feed.getAccount().getId().equals(parentComment.getAccount().getId());
    }

    private Comment getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));
        if (!comment.isVisibility()) throw new CommentException(StatusCode.IS_DELETED_COMMENT);

        return comment;
    }

    private List<ChildCommentCard> getChildComment(Account loginAccount, Comment comment, List<Friendship> friendships) {
        List<Comment> childList = comment.getChildList();
        return childList.stream()
                .filter(BaseEntity::isVisibility)
                .map(child -> ChildCommentCard.of(
                comment, child, friendshipService.getFriendNickname(friendships, child.getAccount()),
                isCommentAuthor(loginAccount, child))).toList();
    }

    private boolean isCommentAuthor(Account account, Comment comment) {
        return comment.getAccount().getId().equals(account.getId());
    }

    private boolean isFeedAuthor(Account account, Feed feed) {
        return feed.getAccount().getId().equals(account.getId());
    }

    private static boolean checkIsSameAccount(Account author, Account loginAccount) {
        return author.getId().equals(loginAccount.getId());
    }
}
