package tinqle.tinqleServer.domain.account.template;

import tinqle.tinqleServer.domain.account.model.*;

public class AccountTemplate {

    private static final Long ID_A = 1L;
    private static final Long ID_B = 2L;
    private static final String DUMMY_PASSWORD_A = "test1@naver.com";
    private static final String DUMMY_PASSWORD_B = "test2@naver.com";
    private static final String DUMMY_NICKNAME_A = "test1";
    private static final String DUMMY_NICKNAME_B = "test2";
    private static final String DUMMY_SOCIAL_EMAIL_A = "test1@KAKAO";
    private static final String DUMMY_SOCIAL_EMAIL_B = "test2@GOOGLE";
    private static final String CODE_A = "123456";
    private static final String CODE_B = "ABCDEF";
    private static final AccountStatus ACCOUNT_STATUS = AccountStatus.NORMAL;
    private static final Status STATUS_A = Status.HAPPY;
    private static final Status STATUS_B = Status.SAD;
    private static final SocialType SOCIAL_TYPE_A = SocialType.KAKAO;
    private static final SocialType SOCIAL_TYPE_B = SocialType.GOOGLE;
    private static final Role ROLE = Role.ROLE_USER;
    private static final boolean IS_RECEIVED_PUSH_NOTIFICATION = true;
    private static Account createAccount(Long id, String password, String nickname, String socialEmail,
                                         String code, AccountStatus accountStatus, Status status, SocialType socialType,
                                         Role role, boolean isReceivedPushNotification) {
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
                .isReceivedPushNotification(isReceivedPushNotification).build();
    }

    public static Account createDummyAccountA() {
        return createAccount(ID_A, DUMMY_PASSWORD_A, DUMMY_NICKNAME_A, DUMMY_SOCIAL_EMAIL_A, CODE_A, ACCOUNT_STATUS,
                STATUS_A, SOCIAL_TYPE_A,ROLE, IS_RECEIVED_PUSH_NOTIFICATION);
    }
    public static Account createDummyAccountB() {
        return createAccount(ID_B, DUMMY_PASSWORD_B, DUMMY_NICKNAME_B, DUMMY_SOCIAL_EMAIL_B, CODE_B, ACCOUNT_STATUS,
                STATUS_B, SOCIAL_TYPE_B,ROLE, IS_RECEIVED_PUSH_NOTIFICATION);
    }

}
