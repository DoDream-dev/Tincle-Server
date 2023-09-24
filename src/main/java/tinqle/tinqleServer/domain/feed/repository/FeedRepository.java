package tinqle.tinqleServer.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.feed.model.Feed;


public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {

}
