package tinqle.tinqleServer.domain.test.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto;
import tinqle.tinqleServer.domain.comment.service.CommentService;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto;
import tinqle.tinqleServer.domain.emoticon.service.EmoticonService;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Hidden
public class TestController {

    private final FriendshipService friendshipService;
    private final FeedService feedService;
    private final EmoticonService emoticonService;
    private final CommentService commentService;

    @GetMapping("/manage/{id}")
    public ApiResponse<?> manage(
            @PathVariable Long id,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(friendshipService.getFriendshipManage(id, pageable, cursorId));
    }

    @GetMapping("/feeds/{id}")
    public ApiResponse<?> getFeeds(
            @PathVariable Long id,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(feedService.getFeeds(id, pageable, cursorId));
    }

    @GetMapping("/feeds/{feedId}/{accountId}")
    public ApiResponse<EmoticonResponseDto.GetNicknameListResponse> getEmoticonReactAccounts(
            @PathVariable Long feedId,
            @PathVariable Long accountId) {
        return success(emoticonService.getEmoticonReactAccount(accountId, feedId));
    }

    @GetMapping("/{feedId}/comments/{accountId}")
    public ApiResponse<SliceResponse<CommentResponseDto.CommentCardResponse>> getComments(
            @PathVariable Long feedId,
            @PathVariable Long accountId,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(commentService.getCommentsByFeed(accountId, feedId, pageable, cursorId));
    }

}
