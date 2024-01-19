package tinqle.tinqleServer.domain.emoticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.comment.model.Comment;
import tinqle.tinqleServer.domain.emoticon.model.Emoticon;
import tinqle.tinqleServer.domain.emoticon.model.EmoticonType;
import tinqle.tinqleServer.domain.feed.model.Feed;

import java.util.Optional;

public interface EmoticonRepository extends JpaRepository<Emoticon, Long>, EmoticonRepositoryCustom {

    Optional<Emoticon> findByAccountAndFeedAndEmoticonType(Account account, Feed feed, EmoticonType emoticonType);
    Optional<Emoticon> findByAccountAndComment(Account account, Comment comment);

}
