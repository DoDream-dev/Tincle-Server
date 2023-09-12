package tinqle.tinqleServer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /**
     * Common
     */
    INTERNAL_SERVER_ERROR(500,  -1, "internal server error."),



    /**
     * Auth
     */

    LOGIN(200, 1001, "account exist, process login."),
    SIGNUP_COMPLETE(200, 1011, "signup complete, access token is issued."),


    // fail
    FILTER_ACCESS_DENIED(401, 1000, "access denied."),
    FILTER_ROLE_FORBIDDEN(403, 1010, "role forbidden"),
    SIGNUP_TOKEN_ERROR(400, 1020, "invalid sign up token error."),
    NEED_TO_SIGNUP(404, 1030, "need to signup, X-ACCESS-TOKEN is issued."),

    ENCRYPTION_FAILURE(400, 1040, "encryption failure"),
    DECRYPTION_FAILURE(400,1050,"decryption failure"),
    IS_NOT_REFRESH(400, 1060, "this token is not refresh token."),
    EXPIRED_REFRESH(400,1070,"expired refresh token"),
    IS_NOT_CORRECT_REFRESH(400,1080,"this token is not correct refresh token"),
    CODE_CREATE_ERROR(500,1090,"code create error, retry please"),
    ALREADY_EXIST_ACCOUNT(400,1100,"already exist account"),

    /**
     * User
     */
    NOT_FOUND_ACCOUNT(404, 2000, "not found account error."),
    SOCIAL_TYPE_ERROR(400,2010,"invalid social type error."),


    /**
     * Image
     */

    INVALID_INPUT_VALUE(400, 7010, "invalid input value."),
    AWS_S3_UPLOAD_FAIL(400, 7020, "AWS S3 upload fail."),
    S3_FILE_TYPE_INVALID(400, 7030, "invalid file type."),
    FAIL_UPLOAD_IMAGES(400, 7040, "fail to upload image.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
