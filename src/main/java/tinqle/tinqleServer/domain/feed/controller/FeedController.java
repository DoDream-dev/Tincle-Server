package tinqle.tinqleServer.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.PageResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto.CreateFeedRequest;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.CreateFeedResponse;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.DeleteFeedResponse;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.FeedCardResponse;
import tinqle.tinqleServer.domain.feed.service.FeedService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@Tag(name = TAG_FEED, description = TAG_FEED_DESCRIPTION)
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = FEED_GET)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping
    public ApiResponse<PageResponse<FeedCardResponse>> getFeeds(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(feedService.getFeeds(principalDetails.getId(), pageable, cursorId));
    }

    @Operation(summary = FEED_CREATE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping
    public ApiResponse<CreateFeedResponse> createFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateFeedRequest createFeedRequest) {
        return success(feedService.createFeed(principalDetails.getId(), createFeedRequest));
    }

    @Operation(summary = FEED_DELETE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @DeleteMapping("/{feedId}")
    public ApiResponse<DeleteFeedResponse> deleteFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(feedService.deleteFeed(principalDetails.getId(), feedId));
    }
}
