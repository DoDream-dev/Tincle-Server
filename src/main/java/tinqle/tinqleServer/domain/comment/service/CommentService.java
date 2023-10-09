package tinqle.tinqleServer.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.comment.dto.request.CommentRequestDto.CreateCommentRequest;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.ChildCommentCard;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CommentCardResponse;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CreateCommentResponse;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.comment.repository.CommentRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final AccountService accountService;
    private final FeedService feedService;
    private final FriendshipService friendshipService;
    private final FriendshipRepository friendshipRepository;
    private final CommentRepository commentRepository;

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

    @Transactional
    public CreateCommentResponse createParentComment(Long accountId, Long feedId, CreateCommentRequest createCommentRequest) {
        Account loginAccount = accountService.getAccountById(accountId);
        Feed feed = feedService.getFeedById(feedId);

        Comment parentComment = Comment.builder()
                .account(loginAccount)
                .feed(feed)
                .parent(null)
                .content(createCommentRequest.content())
                .build();
        commentRepository.save(parentComment);

        return CreateCommentResponse.of(parentComment, loginAccount);
    }

    private List<ChildCommentCard> getChildComment(Account loginAccount, Comment comment, List<Friendship> friendships) {
        List<Comment> childList = comment.getChildList();
        return childList.stream().map(child -> ChildCommentCard.of(
                comment, child, friendshipService.getFriendNickname(friendships, child.getAccount()),
                isCommentAuthor(loginAccount, child))).toList();
    }

    private boolean isCommentAuthor(Account account, Comment comment) {
        return comment.getAccount().getId().equals(account.getId());
    }
}
