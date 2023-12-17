package tinqle.tinqleServer.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Long countByParent(Comment parentComment);
}
