package tinqle.tinqleServer.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.feed.dto.request.FeedRequestDto.FeedRequest;
import tinqle.tinqleServer.domain.feed.dto.response.FeedResponseDto.FeedResponse;
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

    @Operation(summary = FEED_GET, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping
    public ApiResponse<SliceResponse<FeedCardResponse>> getFeeds(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(feedService.getFeeds(principalDetails.getId(), pageable, cursorId));
    }

    @Operation(summary = FEED_GET_DETAIL, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/{feedId}")
    public ApiResponse<FeedCardResponse> getFeedsDetail(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(feedService.getFeedDetail(principalDetails.getId(), feedId));
    }

    @Operation(summary = FEED_CREATE, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping
    public ApiResponse<FeedResponse> createFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid FeedRequest createFeedRequest) {
        return success(feedService.createFeed(principalDetails.getId(), createFeedRequest));
    }

    @Operation(summary = KNOCK_FEED_CREATE, description = KNOCK_FEED_CREATE_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/knock")
    public ApiResponse<FeedResponse> createKnockFeed(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid FeedRequest feedRequest) {
        return success(feedService.createKnockFeed(principalDetails.getId(), feedRequest));
    }

    @Operation(summary = FEED_DELETE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @DeleteMapping("/{feedId}")
    public ApiResponse<DeleteFeedResponse> deleteFeed(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(feedService.deleteFeed(principalDetails.getId(), feedId));
    }

    @Operation(summary = FEED_UPDATE, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/{feedId}")
    public ApiResponse<FeedResponse> updateFeed(
            @PathVariable Long feedId,
            @RequestBody FeedRequest feedRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(feedService.updateFeed(principalDetails.getId(), feedId, feedRequest));
    }
}
