package tinqle.tinqleServer.domain.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.comment.model.Comment;

public interface CommentRepositoryCustom {

    Slice<Comment> findAllByFeed(Long feedId, Pageable pageable, Long cursorId);
}
