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
    VALID_ERROR(400, -2, "request valid error"),



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
    IS_NOT_CORRECT_REFRESH(400,1080,"not found refresh token."),
    CODE_CREATE_ERROR(500,1090,"code create error, retry please"),
    ALREADY_EXIST_ACCOUNT(400,1100,"already exist account"),

    /**
     * Account
     */
    NOT_FOUND_ACCOUNT(404, 2010, "not found account error."),
    SOCIAL_TYPE_ERROR(400,2020,"invalid social type error."),
    NOT_FOUND_ACCOUNT_CODE(404,2030, "not found account code error"),
    NICKNAME_VALIDATE_ERROR(400,2040,"invalid nickname error"),
    NOT_FOUND_STATUS(404,2050,"not found status error"),
    SAME_STATUS_ERROR(400,2060,"reqeust status equal account's status"),
    SAME_NICKNAME_ERROR(400, 2070, "request nickname equal account's nickname"),

    /**
     * Friendship
     */
    DUPLICATE_FRIENDSHIP_REQUEST(400, 3010,"already exist friendship request"),
    ALREADY_FRIENDSHIP(400, 3020,"already exist friend relationship"),
    NOT_FOUND_FRIENDSHIP_REQUEST(404,3030,"not found friendship request error"),
    IS_NOT_CORRECT_FRIENDSHIP_REQUEST(400, 3040, "this friendship is wrong"),
    NOT_FOUND_FRIENDSHIP(404, 3050, "not found friendship error"),

    /**
     * Feed
     */
    NOT_FOUND_FEED(404,4010,"not found feed error"),
    NOT_AUTHOR_FEED(403, 4020, "not an author of this feed."),
    IS_DELETED_FEED(404, 4030, "this feed is already deleted."),

    /**
     * Emoticon
     */
    NOT_FOUND_EMOTICON_TYPE(404,5010,"not found emoticon error"),

    /**
     * Comment
     */
    NOT_FOUND_COMMENT(404, 6010, "not found comment error"),
    IS_DELETED_COMMENT(404, 6020, "this comment is already deleted"),


    /**
     * Image
     */

    INVALID_INPUT_VALUE(400, 7010, "invalid input value."),
    AWS_S3_UPLOAD_FAIL(400, 7020, "AWS S3 upload fail."),
    S3_FILE_TYPE_INVALID(400, 7030, "invalid file type."),
    FAIL_UPLOAD_IMAGES(400, 7040, "fail to upload image."),
    AWS_S3_DELETE_FAIL(400, 7050, "fail to delete image.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
