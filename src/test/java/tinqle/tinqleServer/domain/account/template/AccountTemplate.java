package tinqle.tinqleServer.domain.account.template;

import tinqle.tinqleServer.domain.account.model.*;

public class AccountTemplate {

    private static final Long ID_A = 1L;
    private static final Long ID_B = 2L;
    private static final Long ID_C = 3L;
    private static final String DUMMY_PASSWORD_A = "test1@naver.com";
    private static final String DUMMY_PASSWORD_B = "test2@naver.com";
    private static final String DUMMY_PASSWORD_C = "test3@naver.com";
    private static final String DUMMY_NICKNAME_A = "test1";
    private static final String DUMMY_NICKNAME_B = "test2";
    private static final String DUMMY_NICKNAME_C = "test3";
    private static final String DUMMY_SOCIAL_EMAIL_A = "test1@KAKAO";
    private static final String DUMMY_SOCIAL_EMAIL_B = "test2@GOOGLE";
    private static final String DUMMY_SOCIAL_EMAIL_C = "test3@GOOGLE";
    private static final String CODE_A = "123456";
    private static final String CODE_B = "ABCDEF";
    private static final String CODE_C = "GHIGKL";
    private static final AccountStatus ACCOUNT_STATUS = AccountStatus.NORMAL;
    private static final Status STATUS_A = Status.HAPPY;
    private static final Status STATUS_B = Status.SAD;
    private static final Status STATUS_C = Status.MAD;
    private static final SocialType SOCIAL_TYPE_A = SocialType.KAKAO;
    private static final SocialType SOCIAL_TYPE_B = SocialType.GOOGLE;
    private static final Role ROLE = Role.ROLE_USER;
    private static final boolean IS_RECEIVED_PUSH_NOTIFICATION = true;
    private static final String IMAGE_URL_A = "imageUrlA";
    private static final String IMAGE_URL_B = "imageUrlB";
    private static final String IMAGE_URL_C = "imageUrlC";
    private static Account createAccount(Long id, String password, String nickname, String socialEmail,
                                         String code, AccountStatus accountStatus, Status status, SocialType socialType,
                                         Role role, boolean isReceivedPushNotification, String profileImageUrl) {
        return Account.builder()
                .id(id)
                .password(password)
                .nickname(nickname)
                .socialEmail(socialEmail)
                .code(code)
                .accountStatus(accountStatus)
                .status(status)
                .socialType(socialType)
                .role(role)
                .isReceivedPushNotification(isReceivedPushNotification)
                .profileImageUrl(profileImageUrl).build();
    }

    // 인메모리 DB용 더미데이터는 ID 제거
    private static Account createDummyAccountAExceptId(String password, String nickname, String socialEmail,
                                                      String code, AccountStatus accountStatus, Status status, SocialType socialType,
                                                      Role role, boolean isReceivedPushNotification, String profileImageUrl) {
        return Account.builder()
                .password(password)
                .nickname(nickname)
                .socialEmail(socialEmail)
                .code(code)
                .accountStatus(accountStatus)
                .status(status)
                .socialType(socialType)
                .role(role)
                .isReceivedPushNotification(isReceivedPushNotification)
                .profileImageUrl(profileImageUrl).build();
    }

    public static Account createDummyAccountA() {
        return createAccount(ID_A, DUMMY_PASSWORD_A, DUMMY_NICKNAME_A, DUMMY_SOCIAL_EMAIL_A, CODE_A, ACCOUNT_STATUS,
                STATUS_A, SOCIAL_TYPE_A,ROLE, IS_RECEIVED_PUSH_NOTIFICATION, IMAGE_URL_A);
    }
    public static Account createDummyAccountB() {
        return createAccount(ID_B, DUMMY_PASSWORD_B, DUMMY_NICKNAME_B, DUMMY_SOCIAL_EMAIL_B, CODE_B, ACCOUNT_STATUS,
                STATUS_B, SOCIAL_TYPE_B,ROLE, IS_RECEIVED_PUSH_NOTIFICATION, IMAGE_URL_B);
    }
    public static Account createDummyAccountC() {
        return createAccount(ID_C, DUMMY_PASSWORD_C, DUMMY_NICKNAME_C, DUMMY_SOCIAL_EMAIL_C, CODE_C, ACCOUNT_STATUS,
                STATUS_C, SOCIAL_TYPE_B,ROLE, IS_RECEIVED_PUSH_NOTIFICATION, IMAGE_URL_C);
    }

    public static Account createDummyAccountA_ExceptId() {
        return createDummyAccountAExceptId(DUMMY_PASSWORD_A, DUMMY_NICKNAME_A, DUMMY_SOCIAL_EMAIL_A, CODE_A, ACCOUNT_STATUS,
                STATUS_A, SOCIAL_TYPE_A,ROLE, IS_RECEIVED_PUSH_NOTIFICATION, IMAGE_URL_A);
    }

    public static Account createDummyAccountB_ExceptId() {
        return createDummyAccountAExceptId( DUMMY_PASSWORD_B, DUMMY_NICKNAME_B, DUMMY_SOCIAL_EMAIL_B, CODE_B, ACCOUNT_STATUS,
                STATUS_B, SOCIAL_TYPE_B,ROLE, IS_RECEIVED_PUSH_NOTIFICATION, IMAGE_URL_B);
    }

    public static Account createDummyAccountExceptId(String nickname) {
        return Account.builder()
                .nickname(nickname)
                .build();
    }

}
