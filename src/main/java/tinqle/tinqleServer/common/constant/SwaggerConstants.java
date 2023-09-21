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
    public static final String ACCOUNT_OTHERS = "다른 계정 정보 조회";
    public static final String ACCOUNT_SEARCH_CODE = "코드 검색";
    public static final String ACCOUNT_UPDATE_NICKNAME = "내 닉네임 변경";
    public static final String ACCOUNT_UPDATE_STATUS = "내 상태 변경";

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

    /**
     * IMAGE
     */

    public static final String TAG_IMAGE = "Image";
    public static final String TAG_IMAGE_DESCRIPTION = "Image API";
    public static final String IMAGE_UPLOAD = "이미지 업로드";
    public static final String IMAGE_UPLOAD_DESCRIPTION = """
            💡 파일 확장자 :\s
            { png, jpg, jpeg, gif }
            
            💡`type` 종류 : \n
            `feed` : 피드 이미지
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
