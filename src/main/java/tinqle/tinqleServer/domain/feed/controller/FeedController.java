package tinqle.tinqleServer.domain.feed.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.PageResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto.CreateFeedRequest;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.CreateFeedResponse;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.FeedCardResponse;
import tinqle.tinqleServer.domain.feed.service.FeedService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ApiResponse<PageResponse<FeedCardResponse>> getFeeds(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(feedService.getFeeds(principalDetails.getId(), pageable, cursorId));
    }

    @PostMapping
    public ApiResponse<CreateFeedResponse> createFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateFeedRequest createFeedRequest) {
        return success(feedService.createFeed(principalDetails.getId(), createFeedRequest));
    }
}
