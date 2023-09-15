package tinqle.tinqleServer.common.constant;

public class SwaggerConstants {

    /**
     * swagger
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
}
