package tinqle.tinqleServer.domain.notification.dto;

import lombok.Builder;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;
import tinqle.tinqleServer.domain.notification.model.NotificationType;

import static tinqle.tinqleServer.common.constant.GlobalConstants.NOTIFICATION_TITLE;
import static tinqle.tinqleServer.domain.notification.model.NotificationType.*;
import static tinqle.tinqleServer.util.LengthAdjustUtil.adjustLengthFifteen;

public class NotificationDto {

    public record NotifyParams(
            Account receiver, Account sender, NotificationType type, Long redirectTargetId, String title, String content
    ) {
        @Builder
        public NotifyParams{}

        // 친구 요청 생성 알림
        public static NotifyParams ofCreateFriendshipRequest(FriendshipRequest friendshipRequest) {
            String content = """
                    %s 님이 나에게 친구 요청을 보냈어요.
                    """.formatted(friendshipRequest.getRequestAccount().getNickname());    // 추후 static 변환

            return NotifyParams.builder()
                    .receiver(friendshipRequest.getResponseAccount())   // 친구 요청 받는 사람
                    .sender(friendshipRequest.getRequestAccount())
                    .type(CREATE_FRIENDSHIP_REQUEST)
                    .redirectTargetId(friendshipRequest.getId())
                    .title(NOTIFICATION_TITLE)
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
                    .sender(friendshipRequest.getResponseAccount())
                    .type(APPROVE_FRIENDSHIP_REQUEST)
                    .redirectTargetId(friendshipRequest.getId())
                    .title(NOTIFICATION_TITLE)
                    .content(content)
                    .build();
        }

        //피드에 하트 이모티콘 반응 알림
        public static NotifyParams ofReactHeartEmoticonOnFeed(String friendNickname, Account sender, Feed feed) {
            String content = """
                    %s 님이 "%s"를 좋아해요.
                    """.formatted(friendNickname, adjustLengthFifteen(feed.getContent()));

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, REACT_EMOTICON_ON_FEED);
        }

        //피드에 웃음 이모티콘 반응 알림
        public static NotifyParams ofReactSmileEmoticonOnFeed(String friendNickname, Account sender, Feed feed) {
            String content = """
                    %s 님이 "%s"에 웃었어요.
                    """.formatted(friendNickname, adjustLengthFifteen(feed.getContent()));

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, REACT_EMOTICON_ON_FEED);
        }

        //피드에 슬픔 이모티콘 반응 알림
        public static NotifyParams ofReactSadEmoticonOnFeed(String friendNickname, Account sender, Feed feed) {
            String content = """
                    %s 님이 "%s"에 슬퍼했어요.
                    """.formatted(friendNickname, adjustLengthFifteen(feed.getContent()));

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, REACT_EMOTICON_ON_FEED);
        }

        //피드에 놀람 이모티콘 반응 알림
        public static NotifyParams ofReactSurpriseEmoticonOnFeed(String friendNickname, Account sender, Feed feed) {
            String content = """
                    %s 님이 "%s"에 놀랐어요.
                    """.formatted(friendNickname, adjustLengthFifteen(feed.getContent()));

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, REACT_EMOTICON_ON_FEED);
        }

        // 내 피드에 댓글 생성 시 알림
        public static NotifyParams ofCreateCommentOnMyFeed(String friendNickname, Account sender, Feed feed, String detail) {
            String content = """
                    %s 님이 내 글에 댓글을 남겼어요: %s
                    """.formatted(friendNickname, detail);

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, CREATE_COMMENT_ON_FEED);
        }

        // 내 피드에 내가 댓글 생성 시 알림
        public static NotifyParams ofCreateCommentAuthorIsFeedAuthor(
                Account receiver, Account sender, String friendNickname, Feed feed, String detail) {
            String content = """
                    %s 님이 자신이 쓴 글에 댓글을 남겼어요: %s
                    """.formatted(friendNickname, detail);

            return createNotifyParamsByBuilder(receiver, sender, feed.getId(), content, CREATE_COMMENT_ON_FEED);
        }

        // 내 피드에 대댓글 생성 시 알림
        public static NotifyParams ofCreateChildCommentOnMyFeed(String friendNickname, Account sender, Feed feed, String detail) {
            String content = """
                    %s 님이 내 글에 대댓글을 남겼어요: %s
                    """.formatted(friendNickname, detail);

            return createNotifyParamsByBuilder(feed.getAccount(), sender, feed.getId(), content, CREATE_CHILD_COMMENT_ON_FEED);
        }

        // 내 댓글에 대댓글 생성 시 알림
        public static NotifyParams ofCreateChildCommentOnMyParentComment(String nickname, Account sender, Comment parentComment, String detail) {
            String content = """
                    %s 님이 내 댓글에 대댓글을 남겼어요: %s
                    """.formatted(nickname, detail);

            return createNotifyParamsByBuilder(parentComment.getAccount(), sender, parentComment.getFeed().getId(), content, CREATE_CHILD_COMMENT_ON_FEED);
        }

        // 내가 참여한 댓글에 대댓글이 달렸을 시
        public static NotifyParams ofCreateChildCommentOnParentComment(Account receiver, Account sender, String nickname, Feed feed, String detail) {
            String content = """
                    %s 님이 내가 참여한 댓글에 대댓글을 달았어요: %s
                    """.formatted(nickname, detail);

            return createNotifyParamsByBuilder(receiver, sender, feed.getId(), content, CREATE_CHILD_COMMENT_ON_FEED);
        }

        public static NotifyParams ofCreateMessageBoxToMe(Account receiver, Account sender, MessageBox messageBox) {
            String content = """
                    익명 쪽지가 도착했어요!
                    """;

            return createNotifyParamsByBuilder(receiver, sender, messageBox.getId(), content, CREATE_MESSAGE_BOX);
        }

        public static NotifyParams ofSendKnockMessage(Account receiver, Account sender, String friendNickname) {
            String content = """
                    %s 님이 지금 뭐하는지 궁금해해요.
                    """.formatted(friendNickname);
            return createNotifyParamsByBuilder(receiver, sender, null, content, SEND_KNOCK);
        }

        public static NotifyParams ofCreateKnockFeedMessage(Account receiver, String friendshipNickname, Feed feed) {
            String content = """
                    %s 님이 지금 뭐하는지 올렸어요.
                    """.formatted(friendshipNickname);
            return createNotifyParamsByBuilder(receiver, feed.getAccount(), feed.getId(), content, CREATE_KNOCK_FEED);
        }

        private static NotifyParams createNotifyParamsByBuilder(Account receiver, Account sender, Long redirectTargetId, String content, NotificationType type) {
            return NotifyParams.builder()
                    .receiver(receiver)
                    .sender(sender)
                    .type(type)
                    .redirectTargetId(redirectTargetId)
                    .content(content)
                    .title(NOTIFICATION_TITLE)
                    .build();
        }
    }

}
