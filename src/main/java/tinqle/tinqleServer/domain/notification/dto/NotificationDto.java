package tinqle.tinqleServer.domain.notification.dto;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

import static tinqle.tinqleServer.domain.notification.model.NotificationType.CRAETE_COMMENT_ON_PARENT_COMMENT;
import static tinqle.tinqleServer.domain.notification.model.NotificationType.CREATE_COMMENT_ON_FEED;

public class NotificationDto {

    public record NotifyParams(
            Account receiver, NotificationType type, Long redirectTargetId, String title, String content
    ) {
        @Builder
        public NotifyParams{}

        // 친구 요청 생성 알림
        public static NotifyParams ofCreateFriendshipRequest(FriendshipRequest friendshipRequest) {
            String content = """
                    %s 님이 나에게 친구 요청을 보냈어요.
                    """.formatted(friendshipRequest.getRequestAccount().getNickname());

            return NotifyParams.builder()
                    .receiver(friendshipRequest.getResponseAccount())   // 친구 요청 받는 사람
                    .type(NotificationType.CREATE_FRIENDSHIP_REQUEST)
                    .redirectTargetId(friendshipRequest.getId())
                    .content(content)
                    .build();
        }

        // 친구 요청 수락 알림
        public static NotifyParams ofApproveFriendshipRequest(FriendshipRequest friendshipRequest) {
            String content = """
                    %s 님이 친구 요청을 수락했어요.
                    """.formatted(friendshipRequest.getResponseAccount().getNickname());

            return NotifyParams.builder()
                    .receiver(friendshipRequest.getRequestAccount())    // 친구 요청 한 사람
                    .type(NotificationType.APPROVE_FRIENDSHIP_REQUEST)
                    .redirectTargetId(friendshipRequest.getId())
                    .content(content)
                    .build();
        }

        // 피드에 이모티콘 반응 알림
        public static NotifyParams ofReactEmoticonOnFeed(String friendNickname, Feed feed) {
            String content = """
                    %s 님이 내 글에 반응했어요.
                    """.formatted(friendNickname);

            return createNotifyParamsByBuilder(feed.getAccount(), feed.getId(), content, CREATE_COMMENT_ON_FEED);
        }

        // 내 피드에 댓글 생성 시 알림
        public static NotifyParams ofCreateCommentOnMyFeed(String friendNickname, Feed feed) {
            String content = """
                    %s 님이 내 글에 댓글을 달았어요.
                    """.formatted(friendNickname);

            return createNotifyParamsByBuilder(feed.getAccount(), feed.getId(), content, CREATE_COMMENT_ON_FEED);
        }

        // 내 피드에 내가 댓글 생성 시 알림
        public static NotifyParams ofCreateCommentAuthorIsFeedAuthor(Account receiver, String friendNickname, Feed feed) {
            String content = """
                    %s 님이 자신이 쓴 글에 댓글을 달았어요.
                    """.formatted(friendNickname);

            return createNotifyParamsByBuilder(receiver, feed.getId(), content, CREATE_COMMENT_ON_FEED);
        }

        // 내 피드에 대댓글 생성 시 알림
        public static NotifyParams ofCreateChildCommentOnMyFeed(String friendNickname, Feed feed) {
            String content = """
                    %s 님이 내 글에 대댓글을 달았어요.
                    """.formatted(friendNickname);

            return createNotifyParamsByBuilder(feed.getAccount(), feed.getId(), content, CRAETE_COMMENT_ON_PARENT_COMMENT);
        }

        // 내 댓글에 대댓글 생성 시 알림
        public static NotifyParams ofCreateChildCommentOnMyParentComment(String nickname, Comment parentComment) {
            String content = """
                    %s 님이 내 댓글에 대댓글을 달았어요.
                    """.formatted(nickname);

            return createNotifyParamsByBuilder(parentComment.getAccount(), parentComment.getFeed().getId(), content, CRAETE_COMMENT_ON_PARENT_COMMENT);
        }

        // 내가 참여한 댓글에 대댓글이 달렸을 시
        public static NotifyParams ofCreateChildCommentOnParentComment(Account receiver, String nickname, Feed feed) {
            String content = """
                    %s 님이 내가 참여한 댓글에 대댓글을 달았어요.
                    """.formatted(nickname);

            return createNotifyParamsByBuilder(receiver, feed.getId(), content, CRAETE_COMMENT_ON_PARENT_COMMENT);
        }

        private static NotifyParams createNotifyParamsByBuilder(Account receiver,Long redirectTargetId, String content, NotificationType type) {
            return NotifyParams.builder()
                    .receiver(receiver)
                    .type(type)
                    .redirectTargetId(redirectTargetId)
                    .content(content)
                    .title("팅클")
                    .build();
        }
    }

}
