package tinqle.tinqleServer.domain.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    /**
     * 친구 요청
     */
    CREATE_FRIENDSHIP_REQUEST,

    /**
     * 친구 요청 수락
     */
    APPROVE_FRIENDSHIP_REQUEST,


    /**
     * 익명 쪽지 생성
     */
    CREATE_MESSAGE_BOX,

    /**
     * 게시글에 이모티콘 반응
     */
    REACT_EMOTICON_ON_FEED,

    /**
     * 게시글에 댓글 생성
     */
    CREATE_COMMENT_ON_FEED,

    /**
     * 댓글에 대댓글 생성
     */
    CREATE_CHILD_COMMENT_ON_FEED,

    /**
     * 지금 뭐해? 알림
     */
    SEND_KNOCK,
    TEST_USER_ITSELF


}
