package tinqle.tinqleServer.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.model.BaseEntity;
import tinqle.tinqleServer.domain.feed.model.Feed;
import tinqle.tinqleServer.domain.feed.repository.FeedRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedDeleteSchedulerService {

    private final FeedRepository feedRepository;

    @Scheduled(fixedDelay = 60000)
    public void softDeleteFeedSchedule() {
        LocalDateTime time = LocalDateTime.now().minusHours(24);
        List<Feed> deleteTargetFeeds = feedRepository.findAllByCreatedAtBeforeAndVisibilityIsTrue(time);
        deleteTargetFeeds.forEach(
                BaseEntity::softDelete
        );
    }

}
