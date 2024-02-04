package tinqle.tinqleServer.domain.comment.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.dto.vo.CommentCountAndIsReactEmoticonVo;
import tinqle.tinqleServer.domain.comment.model.Comment;

import java.util.List;

import static tinqle.tinqleServer.common.constant.GlobalConstants.DELETE_COMMENT_MESSAGE;
import static tinqle.tinqleServer.util.CustomDateUtil.resolveElapsedTime;

public class CommentResponseDto {

    public record CreateCommentResponse(
            Long commentId,
            String content,
            Long childCount,
            Long accountId,
            String profileImageUrl,
            String friendNickname,
            String status,
            boolean isAuthor,
            String createAt,
            List<ChildCommentCard> childCommentCardList
    ) {
        @Builder
        public CreateCommentResponse{}

        public static CreateCommentResponse of(Comment comment, Account account, String friendNickname, boolean isAuthor, List<ChildCommentCard> childCommentCardList) {
            return CreateCommentResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .childCount((long) comment.getChildList().size())
                    .accountId(comment.getAccount().getId())
                    .profileImageUrl(account.getProfileImageUrl())
                    .friendNickname(friendNickname)
                    .status(comment.getAccount().getStatus().toString())
                    .isAuthor(isAuthor)
                    .createAt(resolveElapsedTime(comment.getCreatedAt()))
                    .childCommentCardList(childCommentCardList).build();
        }
    }

    public record CommentCardResponse(
            Long commentId,
            String content,
            Long childCount,
            Long accountId,
            String profileImageUrl,
            String friendNickname,
            String status,
            boolean isAuthor,
            boolean isReactEmoticon,
            Long emoticonCount,
            String createdAt,
            List<ChildCommentCard> childCommentCardList
    ) {
        @Builder
        public CommentCardResponse {}

        public static CommentCardResponse of(Comment comment, Account account, String friendNickname, boolean isAuthor, CommentCountAndIsReactEmoticonVo commentCountAndIsReactEmoticonVo, List<ChildCommentCard> childCommentCardList) {
            if (!comment.isVisibility() && comment.getParent() == null) {
                return CommentCardResponse.builder()
                        .commentId(comment.getId())
                        .content(DELETE_COMMENT_MESSAGE)
                        .createdAt(resolveElapsedTime(comment.getCreatedAt()))
                        .childCommentCardList(childCommentCardList).build();
            }

            return CommentCardResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .childCount((long) comment.getChildList().size())
                    .accountId(comment.getAccount().getId())
                    .profileImageUrl(account.getProfileImageUrl())
                    .friendNickname(friendNickname)
                    .status(comment.getAccount().getStatus().toString())
                    .isAuthor(isAuthor)
                    .isReactEmoticon(commentCountAndIsReactEmoticonVo.isReactEmoticon)
                    .emoticonCount(commentCountAndIsReactEmoticonVo.count)
                    .createdAt(resolveElapsedTime(comment.getCreatedAt()))
                    .childCommentCardList(childCommentCardList).build();
        }
    }

    public record ChildCommentCard(
            Long parentId,
            Long commentId,
            String content,
            Long accountId,
            String profileImageUrl,
            String friendNickname,
            String status,
            boolean isAuthor,
            boolean isReactEmoticon,
            Long emoticonCount,
            String createdAt
    ) {
        @Builder
        public ChildCommentCard {}

        public static ChildCommentCard of(Comment parentComment, Comment childComment, Account account,
                                          String friendNickname, boolean isAuthor, CommentCountAndIsReactEmoticonVo commentCountAndIsReactEmoticonVo) {
            return ChildCommentCard.builder()
                    .parentId(parentComment.getId())
                    .commentId(childComment.getId())
                    .content(childComment.getContent())
                    .accountId(childComment.getAccount().getId())
                    .profileImageUrl(account.getProfileImageUrl())
                    .friendNickname(friendNickname)
                    .status(childComment.getAccount().getStatus().toString())
                    .isAuthor(isAuthor)
                    .isReactEmoticon(commentCountAndIsReactEmoticonVo.isReactEmoticon)
                    .emoticonCount(commentCountAndIsReactEmoticonVo.count)
                    .createdAt(resolveElapsedTime(childComment.getCreatedAt())).build();
        }
    }

    public record DeleteCommentResponse(
            boolean isDeleted) {}

    public record UpdateCommentResponse(
            Long commentId,
            String content,
            Long accountId,
            String profileImageUrl,
            String friendNickname,
            String status,
            boolean isAuthor,
            String createAt
    ) {
        @Builder
        public UpdateCommentResponse{}

        public static UpdateCommentResponse of(Comment comment, Account account) {
            return UpdateCommentResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .accountId(comment.getAccount().getId())
                    .profileImageUrl(account.getProfileImageUrl())
                    .friendNickname(comment.getAccount().getNickname())
                    .status(comment.getAccount().getStatus().toString())
                    .isAuthor(true)
                    .createAt(resolveElapsedTime(comment.getCreatedAt()))
                    .build();
        }
    }
}
