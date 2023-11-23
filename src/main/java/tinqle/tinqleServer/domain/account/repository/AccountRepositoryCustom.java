package tinqle.tinqleServer.domain.account.repository;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.List;

public interface AccountRepositoryCustom {
    List<Account> findCommentAuthorByFeedDistinctExceptFeedAuthor(Feed feed, Account feedAuthor);
    List<Account> findChildCommentAuthorByParentCommentDistinctExceptAuthors(
            Comment parentComment, Account feedAuthor, Account parentCommentAuthor, Account childCommentAuthor);
}
