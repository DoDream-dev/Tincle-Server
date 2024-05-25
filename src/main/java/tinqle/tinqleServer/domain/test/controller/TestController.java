package tinqle.tinqleServer.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto;
import tinqle.tinqleServer.domain.comment.service.CommentService;
import tinqle.tinqleServer.domain.emoticon.dto.request.EmoticonRequestDto;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto;
import tinqle.tinqleServer.domain.emoticon.service.EmoticonService;
import tinqle.tinqleServer.domain.feed.service.FeedService;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.messageBox.dto.request.MessageBoxRequestDto;
import tinqle.tinqleServer.domain.messageBox.dto.response.MessageBoxResponseDto;
import tinqle.tinqleServer.domain.messageBox.service.MessageBoxService;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.model.NotificationType;
import tinqle.tinqleServer.domain.notification.service.NotificationService;
import tinqle.tinqleServer.domain.test.service.TestService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
//@Tag(name = TAG_TEST, description = TAG_TEST_DESCRIPTION)
public class TestController {

    private final FriendshipService friendshipService;
    private final FeedService feedService;
    private final EmoticonService emoticonService;
    private final CommentService commentService;
    private final TestService testService;
    private final MessageBoxService messageBoxService;
    private final AccountService accountService;
    private final NotificationService notificationService;

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

//    @Operation(summary = TEST_ACCOUNT_DELETE)
    @DeleteMapping("/account/{accountId}")
    public String deleteAccount(@PathVariable Long accountId) {
        testService.deleteAccount(accountId);
        return "ok";
    }

//    @Operation(summary = TEST_FRIENDSHIP_CREATE)
    @PostMapping("/friendship/{accountId}/{requestAccountId}")
    public String createFriendshipTest(
            @PathVariable Long accountId,
            @PathVariable Long requestAccountId) {
        testService.createFriendship(requestAccountId, accountId);
        return "ok";
    }

    @PostMapping("/{accountId}/message/{loginAccountId}")
    public ApiResponse<MessageBoxResponseDto.MessageBoxResponse> createMessageBox(
            @PathVariable Long accountId,
            @PathVariable Long loginAccountId,
            @RequestBody @Valid MessageBoxRequestDto.CreateMessageBoxRequest createMessageBoxRequest) {
        return success(messageBoxService.createMessageBox(loginAccountId, accountId, createMessageBoxRequest));
    }

    @PostMapping("/push/self")
    public String testCreateNotificationMySelf(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Account account = principalDetails.getAccount();
        NotifyParams params = NotifyParams.builder()
                .receiver(account)
                .type(NotificationType.TEST_USER_ITSELF)
                .redirectTargetId(account.getId())
                .title("test")
                .content("자기 자신의 아이디를 리턴")
                .build();
        notificationService.pushMessage(params);

        return "ok";
    }

    @PutMapping("/feeds/{feedId}/{accountId}")
    public ApiResponse<EmoticonResponseDto.EmoticonReactResponse> emoticonReact(
            @PathVariable Long feedId,
            @PathVariable Long accountId,
            @RequestBody EmoticonRequestDto.EmoticonReactRequest emoticonReactRequest) {
        return success(emoticonService.reactEmoticonOnFeed(accountId, feedId, emoticonReactRequest));
    }
}
