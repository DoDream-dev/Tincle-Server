package tinqle.tinqleServer.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;
import static tinqle.tinqleServer.domain.notification.dto.response.NotificationResponseDto.*;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_NOTIFICATION, description = TAG_NOTIFICATION_DESCRIPTION)
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @Operation(summary = NOTIFICATION_ME)
    @GetMapping("/accounts/me")
    public ApiResponse<SliceResponse<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(notificationService.getMyNotifications(principalDetails.getId(), pageable, cursorId));
    }

    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @Operation(summary = NOTIFICATION_DELETE)
    @DeleteMapping("/{notificationId}")
    public ApiResponse<DeleteNotificationResponse> readNotification(@PathVariable Long notificationId,
                                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(notificationService.softDeleteNotification(principalDetails.getId(), notificationId));
    }

    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @Operation(summary = NOTIFICATION_CHECK_UNREAD)
    @GetMapping("/count")
    public ApiResponse<UnReadCountNotificationResponse> countUnreadNotification(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return success(notificationService.countNotReadNotifications(principalDetails.getId()));
    }

    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @Operation(summary = CLICK_NOTIFICATION)
    @PutMapping("/{notificationId}/click")
    public ApiResponse<ClickNotificationResponse> clickNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(notificationService.clickNotification(principalDetails.getId(), notificationId));
    }

    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @Operation(summary = CLICK_ALL_NOTIFICATION)
    @PutMapping("/click")
    public ApiResponse<ClickNotificationResponse> clickAllNotification(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(notificationService.clickAllNotification(principalDetails.getId()));
    }
}
