package tinqle.tinqleServer.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tinqle.tinqleServer.domain.feed.model.FeedImage;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
}
