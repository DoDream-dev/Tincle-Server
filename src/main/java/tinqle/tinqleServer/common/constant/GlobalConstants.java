package tinqle.tinqleServer.common.constant;


public class GlobalConstants {

    /**
     * AWS S3
     */
    public static final int S3_FILE_TYPE_MAX_LENGTH = 15;
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final String S3_OBJECT_NAME_PATTERN = "{0}_{1}.{2}";
    public static final String ACCOUNT_STATUS_URL = "https://tinqle-s3.s3.ap-northeast-2.amazonaws.com/account";

    public static final String[] APPOINTED_URIS = {
            "/auth/reissue",
            "/auth/login",
            "/auth/signup",
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
