package tinqle.tinqleServer.common.constant;

public class SwaggerConstants {

    /**
     * SWAGGER
     */

    public static final String[] SWAGGER_APPOINTED_PATHS = {
            "/**"
    };
    public static final String DEFINITION_TITLE = "팅클 API 명세서";
    public static final String DEFINITION_DESCRIPTION = "\uD83D\uDE80 팅클 Server의 API 명세서입니다.";
    public static final String DEFINITION_VERSION = "v1";

    public static final String SECURITY_SCHEME_NAME = "bearer-key";
    public static final String SECURITY_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
    public static final String SECURITY_SCHEME_DESCRIPTION = "JWT 토큰 키를 입력해주세요.";

    /**
     * AUTH
     */
    public static final String TAG_AUTH = "Auth";
    public static final String TAG_AUTH_DESCRIPTION = "Auth API";
    public static final String AUTH_LOGIN = "로그인";
    public static final String AUTH_LOGIN_DESCRIPTION = """
            기존의 회원이 있다면 로그인 진행 → AccessToken, RefreshToken 발행
            없다면, 회원가입 sign Token 발급
            """;
    public static final String AUTH_SIGNUP = "회원가입";
    public static final String AUTH_REISSUE = "토큰 재발행";
    public static final String AUTH_LOGOUT = "로그아웃";

    /**
     * ACCOUNT
     */
    public static final String TAG_ACCOUNT = "Account";
    public static final String TAG_ACCOUNT_DESCRIPTION = "Account API";
    public static final String ACCOUNT_ME = "내 정보 조회";
    public static final String ACCOUNT_ME_NOTIFICATION_STATUS = "푸시 알림 여부 조회";
    public static final String ACCOUNT_UPDATE_IS_RECEIVED_NOTIFICATION = "푸시 알림 여부 변경";
    public static final String ACCOUNT_UPDATE_IS_RECEIVED_NOTIFICATION_DESCRIPTION = "param에 값이 없으면 true로 바꿉니다!";
    public static final String ACCOUNT_OTHERS = "다른 계정 정보 조회";
    public static final String ACCOUNT_SEARCH_CODE = "코드 검색";
    public static final String ACCOUNT_UPDATE_NICKNAME = "내 닉네임 변경";
    public static final String ACCOUNT_UPDATE_STATUS = "내 상태 변경";
    public static final String ACCOUNT_UPDATE_PROFILE_IMAGE = "내 프로필 이미지 변경";
    public static final String ACCOUNT_REVOKE = "회원 탈퇴";
    public static final String ACCOUNT_CHECK_CODE = "코드 중복 검사";
    public static final String PROFILE_IMAGE_URL_DESCRIPTION = """
            2차 MVP 변경 - profileImageUrl 추가
            """;

    /**
     * FRIENDSHIP
     */
    public static final String TAG_FRIENDSHIP = "Friendship";
    public static final String TAG_FRIENDSHIP_DESCRIPTION = "Friendship API";
    public static final String FRIENDSHIP_GET_MY_CODE = "내 코드 조회";
    public static final String FRIENDSHIP_REQUEST = "친구 요청";
    public static final String FRIENDSHIP_REQUEST_APPROVE = "친구 요청 수락";
    public static final String FRIENDSHIP_REQUEST_REJECT = "친구 요청 거절";
    public static final String FRIENDSHIP_MANAGE = "친구 관리";
    public static final String FRIENDSHIP_NICKNAME_CHANGE = "친구 닉네임 변경";
    public static final String FRIENDSHIP_REQUEST_MESSAGE = "친구 요청 메세지 조회";
    public static final String DELETE_FRIENDSHIP = "친구 삭제";

    /**
     * FEED
     */
    public static final String TAG_FEED = "Feed";
    public static final String TAG_FEED_DESCRIPTION = "Feed API";
    public static final String FEED_GET = "피드 조회";
    public static final String FEED_GET_DETAIL = "피드 상세 조회";
    public static final String FEED_CREATE = "피드 작성";
    public static final String KNOCK_FEED_CREATE = "지금 뭐해? 피드 작성";
    public static final String KNOCK_FEED_CREATE_DESCRIPTION = "지금 뭐해?를 아무도 안했는데 이거로 api 쏘면 에러남";
    public static final String FEED_DELETE = "피드 삭제";
    public static final String FEED_UPDATE = "피드 수정";

    /**
     * EMOTICON
     */

    public static final String TAG_EMOTICON = "Emoticon";
    public static final String TAG_EMOTICON_DESCRIPTION = "Emoticon API";
    public static final String EMOTICON_REACT_ON_FEED = "피드에 이모티콘 반응";
    public static final String EMOTICON_REACT_ON_COMMENT = "댓글에 이모티콘 반응";
    public static final String EMOTICON_GET_REACTION_ACCOUNT = "이모티콘 반응한 사람 조회";

    /**
     * COMMENT
     */

    public static final String TAG_COMMENT = "Comment";
    public static final String TAG_COMMENT_DESCRIPTION = "Comment API";
    public static final String COMMENT_GET = "댓글 조회";
    public static final String PARENT_COMMENT_CREATE = "댓글 생성";
    public static final String CHILD_COMMENT_CREATE = "대댓글 생성";
    public static final String COMMENT_UPDATE = "댓글/대댓글 수정";
    public static final String COMMENT_DELETE = "댓글/대댓글 삭제";

    /**
     * MESSAGE_BOX
     */
    public static final String TAG_MESSAGE_BOX = "MessageBox";
    public static final String TAG_MESSAGE_BOX_DESCRIPTION = "MessageBox API";
    public static final String MESSAGE_BOX_GET = "익명 쪽지함 조회";
    public static final String MESSAGE_BOX_CREATE = "익명 쪽지 생성";

    /**
     * Notification
     */
    public static final String TAG_NOTIFICATION = "Notification";
    public static final String TAG_NOTIFICATION_DESCRIPTION = "Notification API";
    public static final String NOTIFICATION_ME = "알림 목록 조회";
    public static final String NOTIFICATION_DELETE = "알림 삭제";
    public static final String NOTIFICATION_CHECK_UNREAD = "안 읽은 알림 확인";
    public static final String CLICK_NOTIFICATION = "알림 클릭";
    public static final String CLICK_ALL_NOTIFICATION = "알림 전부 클릭";

    /**
     * Knock
     */
    public static final String TAG_KNOCK = "Knock";
    public static final String TAG_KNOCK_DESCRIPTION = "Knock API";
    public static final String SEND_KNOCK = "지금 뭐해? 전송";


    /**
     * TEST
     */

    public static final String TAG_TEST = "Test";
    public static final String TAG_TEST_DESCRIPTION = "Test API";
    public static final String TEST_FRIENDSHIP_CREATE = "친구 강제 생성";
    public static final String TEST_ACCOUNT_DELETE = "계정 강제 삭제";

    /**
     * IMAGE
     */

    public static final String TAG_IMAGE = "Image";
    public static final String TAG_IMAGE_DESCRIPTION = "Image API";
    public static final String IMAGE_UPLOAD_SINGLE= "단일 이미지 업로드";

    public static final String IMAGE_UPLOAD = "이미지 업로드";
    public static final String IMAGE_UPLOAD_DESCRIPTION = """
            💡 파일 확장자 :\s
            { png, jpg, jpeg, gif }
            
            💡`type` 종류 : \n
            `feed` : 피드 이미지
            `account` : 계정 프로필 사진
            """;
    public static final String IMAGE_UPDATE = "이미지 업데이트";
    public static final String IMAGE_UPDATE_DESCRIPTION = """
            💡 파일 확장자 :\s
            { png, jpg, jpeg, gif }
            
            `urlsToDelete` : 변경할 기존 url 주소
            `newFiles`: 업로드할 이미지 파일
            """;
    public static final String IMAGE_DELETE = "이미지 삭제";

}
