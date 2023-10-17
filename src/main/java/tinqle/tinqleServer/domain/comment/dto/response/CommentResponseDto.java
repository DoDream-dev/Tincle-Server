package tinqle.tinqleServer.domain.comment.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;

import java.util.List;

import static tinqle.tinqleServer.util.CustomDateUtil.resolveElapsedTime;

public class CommentResponseDto {

    public record CreateCommentResponse(
            Long parentId,
            Long commentId,
            Long childCount,
            String content,
            Long accountId,
            String nickname,
            String status,
            boolean isAuthor,
            String createAt
    ) {
        @Builder
        public CreateCommentResponse{}

        public static CreateCommentResponse of(Comment comment, Account account) {
            return CreateCommentResponse.builder()
                    .parentId(null)
                    .commentId(comment.getId())
                    .childCount(0L)
                    .content(comment.getContent())
                    .accountId(account.getId())
                    .nickname(account.getNickname())
                    .status(account.getStatus().toString())
                    .isAuthor(true)
                    .createAt(resolveElapsedTime(comment.getCreatedAt()))
                    .build();
        }
    }

    public record CommentCardResponse(
            Long commentId,
            String content,
            Long childCount,
            Long accountId,
            String friendNickname,
            String status,
            boolean isAuthor,
            String createAt,
            List<ChildCommentCard> childCommentCardList
    ) {
        @Builder
        public CommentCardResponse {}

        public static CommentCardResponse of(Comment comment, String friendNickname, boolean isAuthor, List<ChildCommentCard> childCommentCardList) {
            return CommentCardResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .childCount((long) comment.getChildList().size())
                    .accountId(comment.getAccount().getId())
                    .friendNickname(friendNickname)
                    .status(comment.getAccount().getStatus().toString())
                    .isAuthor(isAuthor)
                    .createAt(resolveElapsedTime(comment.getCreatedAt()))
                    .childCommentCardList(childCommentCardList).build();
        }
    }

    public record ChildCommentCard(
            Long parentId,
            Long commentId,
            String content,
            Long accountId,
            String friendNickname,
            String status,
            boolean isAuthor,
            String createAt
    ) {
        @Builder
        public ChildCommentCard {}

        public static ChildCommentCard of(Comment parentComment, Comment childComment, String friendNickname, boolean isAuthor) {
            return ChildCommentCard.builder()
                    .parentId(parentComment.getId())
                    .commentId(childComment.getId())
                    .content(childComment.getContent())
                    .accountId(childComment.getAccount().getId())
                    .friendNickname(friendNickname)
                    .status(childComment.getAccount().getStatus().toString())
                    .isAuthor(isAuthor)
                    .createAt(resolveElapsedTime(childComment.getCreatedAt())).build();
        }
    }
}
