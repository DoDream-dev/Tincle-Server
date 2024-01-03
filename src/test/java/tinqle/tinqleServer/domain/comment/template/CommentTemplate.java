package tinqle.tinqleServer.domain.comment.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.feed.model.Feed;

public class CommentTemplate {

    private static final Long ID_A = 1L;
    private static final String CONTENT = "댓글입니다";

    private static Comment createParentComment(Long id, String content, Account account, Feed feed) {
        return Comment.builder()
                .id(id)
                .account(account)
                .content(content)
                .feed(feed).build();
    }

    private static Comment createParentCommentExceptId(String content, Account account, Feed feed) {
        return Comment.builder()
                .content(content)
                .account(account)
                .feed(feed).build();
    }

    private static Comment createChildComment(
            Long id, String content, Account account, Feed feed, Comment parentComment) {
        return Comment.builder()
                .id(id)
                .content(content)
                .account(account)
                .feed(feed)
                .parent(parentComment).build();
    }

    private static Comment createChildCommentExceptId(
            String content, Account account, Feed feed, Comment parentComment) {
        return Comment.builder()
                .content(content)
                .account(account)
                .feed(feed)
                .parent(parentComment).build();
    }

    public static Comment createDummyParentComment(Account account, Feed feed) {
        return createParentComment(ID_A, CONTENT, account, feed);
    }

    public static Comment createDummyParentCommentExceptId(Account account, Feed feed) {
        return createParentCommentExceptId(CONTENT, account, feed);
    }

    public static Comment createDummyChildCommentExceptId(Account account, Comment comment) {
        return createChildCommentExceptId(CONTENT, account, comment.getFeed(), comment);
    }
}
