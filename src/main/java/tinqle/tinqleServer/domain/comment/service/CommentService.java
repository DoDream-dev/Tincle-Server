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
import tinqle.tinqleServer.domain.comment.dto.request.CommentRequestDto.CommentRequest;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.*;
import tinqle.tinqleServer.domain.comment.dto.vo.CommentCountAndIsReactEmoticonVo;
import tinqle.tinqleServer.domain.comment.exception.CommentException;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.emoticon.repository.EmoticonRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    private final EmoticonRepository emoticonRepository;

    //피드별 댓글 조회
    public SliceResponse<CommentCardResponse> getCommentsByFeed(Long accountId, Long feedId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        feedService.getFeedById(feedId);

        Slice<Comment> comments = commentRepository.findAllByFeed(feedId, pageable, cursorId);
        List<Friendship> friendships = friendshipRepository
                .findAllByAccountSelfAndIsChangeFriendNickname(loginAccount.getId(), true);

        Slice<CommentCardResponse> result = comments.map(comment -> CommentCardResponse.of(comment, comment.getAccount(), friendshipService.getFriendNickname(friendships, comment.getAccount()),
                isCommentAuthor(loginAccount, comment), isReactEmoticonOnComment(comment, loginAccount), getChildComment(loginAccount, comment, friendships)));

        return SliceResponse.of(result);
    }

    //부모 댓글 생성
    @Transactional
    public CreateCommentResponse createParentComment(Long accountId, Long feedId, CommentRequest commentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);

        Comment parentComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(null)
                .content(commentRequest.content())
                .build();
        commentRepository.save(parentComment);

        boolean equals = checkIsSameAccount(feed.getAccount(), loginAccount);
        // 알림 기능
        if (!equals) {
            String friendNickname = friendshipService.getFriendNicknameSingle(feed.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateCommentOnMyFeed(friendNickname, loginAccount, feed, parentComment.getContent()));
        }
        else {
            // 피드 작성자가 댓글 달았을 시 댓글/대댓글에 참여한 모두 조회(피드 작성자 제외)
            List<Account> targetAccounts = accountRepository.findCommentAuthorByFeedDistinctExceptFeedAuthor(feed, feed.getAccount());
            List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);
            targetAccounts.forEach(
                    targetAccount -> notificationService.pushMessage(NotifyParams.ofCreateCommentAuthorIsFeedAuthor(
                            targetAccount, loginAccount, friendshipService.getFriendNicknameByAccountSelf(friendships, targetAccount, loginAccount), feed, parentComment.getContent())));
        }
        return CreateCommentResponse.of(parentComment, parentComment.getAccount(), loginAccount.getNickname(), true, Collections.emptyList());
    }

    //대댓글 생성
    @Transactional
    public ChildCommentCard createChildComment(Long accountId, Long feedId, Long parentCommentId, CommentRequest commentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);
        Comment parentComment = getCommentById(parentCommentId);

        Comment childComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(parentComment)
                .content(commentRequest.content())
                .build();
        commentRepository.save(childComment);

        //대댓글 참여한 모두에게 알림(본인 및 피드 작성자 제외)
        List<Account> targetAccounts = accountRepository.findChildCommentAuthorByParentCommentDistinctExceptAuthors(
                parentComment, feed.getAccount(), parentComment.getAccount(), loginAccount);
        List<Friendship> friendships = friendshipRepository.findAllByAccountFriendAndIsChangeFriendNickname(loginAccount, true);
        targetAccounts.forEach(
                targetAccount -> notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnParentComment(
                        targetAccount, loginAccount, friendshipService.getFriendNicknameByAccountSelf(friendships, targetAccount, loginAccount), feed, childComment.getContent())));

        pushMessageAtDifferentAuthorFeedAndChild(loginAccount, feed, childComment);
        pushMessageAtDifferentAuthorParentAndChild(loginAccount, feed, parentComment, childComment);

        return ChildCommentCard.of(parentComment, childComment, childComment.getAccount(), loginAccount.getNickname(), true, new CommentCountAndIsReactEmoticonVo(0L, false));
    }

    @Transactional
    public ChildCommentCard createCommentChildrenToTarget(Long accountId, Long feedId, Long targetChildrenId, CommentRequest commentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);
        Comment targetComment = getCommentById(targetChildrenId);
        Comment parentComment = targetComment.getParent();
        Comment childComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(parentComment)
                .content(commentRequest.content())
                .build();
        commentRepository.save(childComment);

        pushMessageAtDifferentAuthorFeedAndChild(loginAccount, feed, childComment);
        pushMessageAtDifferentAuthorParentAndChild(loginAccount, feed, targetComment, childComment);
        return ChildCommentCard.of(parentComment, childComment, childComment.getAccount(), loginAccount.getNickname(), true, new CommentCountAndIsReactEmoticonVo(0L, false));
    }

    private void pushMessageAtDifferentAuthorFeedAndChild(Account loginAccount, Feed feed, Comment childComment) {
        if (!isFeedAuthor(loginAccount, feed)) {
            String friendNickname = friendshipService.getFriendNicknameSingle(feed.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnMyFeed(friendNickname, loginAccount, feed, childComment.getContent()));
        }
    }

    private void pushMessageAtDifferentAuthorParentAndChild(Account loginAccount, Feed feed, Comment parentComment, Comment childComment) {
        if (!isCommentAuthor(loginAccount, parentComment) && isDifferentAuthorByFeedAndComment(feed, parentComment)) {
            if (!parentComment.isVisibility()) return;  // 삭제된 댓글이면 리턴

            String friendNickname = friendshipService.getFriendNicknameSingle(parentComment.getAccount(), loginAccount);
            notificationService.pushMessage(NotifyParams.ofCreateChildCommentOnMyParentComment(friendNickname, loginAccount, parentComment, childComment.getContent()));
        }
    }

    @Transactional
    public UpdateCommentResponse updateComment(Long accountId, Long commentId, CommentRequest commentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(loginAccount, comment);

        comment.updateContent(commentRequest.content());

        return UpdateCommentResponse.of(comment, loginAccount);
    }

    @Transactional
    public DeleteCommentResponse deleteComment(Long accountId, Long commentId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(loginAccount, comment);

        if (comment.getParent() != null) {
            Comment parentComment = comment.getParent();

            comment.getAccount().getCommentList().remove(comment);
            comment.getParent().getChildList().remove(comment);
            commentRepository.delete(comment);

            // 삭제된 댓글의 마지막 대댓글 삭제시 댓글 삭제
            deleteAlreadyDeleteParentCommentByLastChildComment(parentComment);

            return new DeleteCommentResponse(true);
        }

        // 대댓글이 없으면 hard delete 대댓글 있으면 soft delete
        deleteCommentDivideCase(comment);

        return new DeleteCommentResponse(true);
    }

    private void deleteAlreadyDeleteParentCommentByLastChildComment(Comment parentComment) {
        if (!parentComment.isVisibility() && parentComment.getChildList().size() == 0) {
            parentComment.getAccount().getCommentList().remove(parentComment);
            commentRepository.delete(parentComment);
        }
    }

    private void deleteCommentDivideCase(Comment comment) {
        Long count = commentRepository.countByParent(comment);
        if ((count > 0)) {
            comment.softDelete();
        }
        else {
            comment.getAccount().getCommentList().remove(comment);
            commentRepository.delete(comment);
        }
    }

    private void validateCommentAuthor(Account loginAccount, Comment comment) {
        if (!loginAccount.getId().equals(comment.getAccount().getId()))
            throw new CommentException(StatusCode.NOT_AUTHOR_COMMENT);
    }

    private static boolean isDifferentAuthorByFeedAndComment(Feed feed, Comment parentComment) {
        return !feed.getAccount().getId().equals(parentComment.getAccount().getId());
    }

    public Comment getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(StatusCode.NOT_FOUND_COMMENT));
        if (!comment.isVisibility()) throw new CommentException(StatusCode.IS_DELETED_COMMENT);

        return comment;
    }

    private List<ChildCommentCard> getChildComment(Account loginAccount, Comment comment, List<Friendship> friendships) {   // 최적화 필요
        List<Comment> childList = comment.getChildList();


        return childList.stream()
                .filter(BaseEntity::isVisibility)
                .map(child -> ChildCommentCard.of(
                comment, child, child.getAccount(), friendshipService.getFriendNickname(friendships, child.getAccount()),
                isCommentAuthor(loginAccount, child), isReactEmoticonOnComment(child, loginAccount))).toList();
    }

    private CommentCountAndIsReactEmoticonVo isReactEmoticonOnComment(Comment comment, Account account) {
        List<Emoticon> emoticons = emoticonRepository.findAllByCommentAndVisibilityIsTrueFetchJoinAccount(comment);
        Optional<Emoticon> emoticonOptional = emoticons.stream().filter(emoticon -> emoticon.getAccount().getId().equals(account.getId()))
                .findFirst();
        return new CommentCountAndIsReactEmoticonVo((long) emoticons.size(), emoticonOptional.isPresent());
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
