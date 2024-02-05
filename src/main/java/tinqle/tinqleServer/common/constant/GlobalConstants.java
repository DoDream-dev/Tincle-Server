package tinqle.tinqleServer.common.constant;


public class GlobalConstants {
    public static final String AT_SIGN = "@";
    public static final String PATTERN_REGEX = "^[0-9a-z]{4,12}$";

    /**
     * AWS S3
     */
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final String S3_OBJECT_NAME_PATTERN = "{0}_{1}.{2}";
    public static final String ACCOUNT_STATUS_URL = "https://tinqle-s3.s3.ap-northeast-2.amazonaws.com/account";

    /**
     * Feed
     */
    public static final String CREATE_WELCOME_FEED_MESSAGE = """
            팅클에 온 걸 환영해요!

            1. 여기에 글을 쓰면 내 친구들이 볼 수 있어요! 모든 글은 24시간이 지나면 사라져요.

            2. 친구에게 내 아이디를 공유해서 팅클 친구를 맺어보세요!

            3. 밑에 있는 아이콘\uD83D\uDE03으로 내 기분과 상태를 표시할 수 있어요!""";


    /**
     * Comment
     */
    public static final String DELETE_COMMENT_MESSAGE = "삭제된 댓글입니다.";

    /**
     * Notification
     */
    public static final String NOTIFICATION_TITLE = "팅클";

    public static final String[] APPOINTED_URIS = {
            "/auth/reissue",
            "/auth/login",
            "/auth/signup",
            "accounts/check/code/**",
            "/hello/**",
            "/test/**",

            "/api-docs/**",
            "/v1/api-docs",
            "/v2/api-docs",
            "/docs/**",
            "/favicon.ico",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/#",
            "/webjars/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/",
            "/csrf",
            "/error",
            "/callback"
    };
}
