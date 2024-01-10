package tinqle.tinqleServer.domain.feed.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.feed.model.Feed;

public class FeedTemplate {

    private static final Long ID_A = 1L;
    private static final String CONTENT_A = "난 A입니다";

    public static Feed createFeed(Long id, String content, Account account, boolean isKnock) {
        return Feed.builder()
                .id(id)
                .content(content)
                .account(account)
                .isKnock(isKnock).build();
    }

    private static Feed createFeedExceptId(String content, Account account, boolean isKnock) {
        return Feed.builder()
                .content(content)
                .account(account)
                .isKnock(isKnock).build();
    }


    public static Feed createDummyFeedExceptId(String content, Account account) {
        return createFeedExceptId(content, account, false);
    }

    public static Feed createDummyFeedA(Account account) {
        return createFeed(ID_A, CONTENT_A, account, false);
    }

    public static Feed createDummyFeedAExceptId(Account account) {
        return createFeedExceptId(CONTENT_A, account, false);
    }
}

