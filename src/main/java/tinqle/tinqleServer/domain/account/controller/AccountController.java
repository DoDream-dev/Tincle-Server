package tinqle.tinqleServer.domain.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.ChangeNicknameRequest;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.ChangeProfileImageUrlRequest;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.*;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.RevokeResponse;
import tinqle.tinqleServer.domain.account.service.AccountService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_ACCOUNT, description = TAG_ACCOUNT_DESCRIPTION)
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    // 내 정보 조회
    @Operation(summary = ACCOUNT_ME, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/me")
    public ApiResponse<MyAccountInfoResponse> getMyAccountInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getMyAccountInfo(principalDetails.getId()));
    }

    // 푸시 알림 여부 변경
    @Operation(summary = ACCOUNT_UPDATE_IS_RECEIVED_NOTIFICATION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/me")
    public ApiResponse<PushNotificationStatusResponse> updatePushNotificationStatus(
            @RequestParam(value = "push-notification", defaultValue = "true") boolean pushNotificationStatus,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return success(accountService.updatePushNotificationStatus(principalDetails.getId(), pushNotificationStatus));
    }

    // 다른 유저 정보 조회
    @Operation(summary = ACCOUNT_OTHERS, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/{accountId}/profile")
    public ApiResponse<OthersAccountInfoResponse> getOthersAccountInfo(
            @PathVariable Long accountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getOthersAccountInfo(principalDetails.getId(), accountId));
    }

    // 코드 검색
    @Operation(summary = ACCOUNT_SEARCH_CODE, description = PROFILE_IMAGE_URL_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/search/code/{code}")
    public ApiResponse<OthersAccountInfoResponse> searchCode(
            @PathVariable String code,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.searchByCode(principalDetails.getId(), code));
    }

    @GetMapping("/check/code/{code}")
    @Operation(summary = ACCOUNT_CHECK_CODE)
    public ApiResponse<CheckCodeResponse> isDuplicatedCode(@PathVariable String code) {
        return success(accountService.isDuplicatedCode(code));
    }

    // 프로필 업데이트(nickname)
    @Operation(summary = ACCOUNT_UPDATE_NICKNAME)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/me/nickname")
    public ApiResponse<UpdateNicknameResponse> updateNickname(
            @RequestBody ChangeNicknameRequest changeNicknameRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.updateNickname(principalDetails.getId(), changeNicknameRequest));
    }

    // 프로필 상태 업데이트(status)
    @Operation(summary = ACCOUNT_UPDATE_STATUS)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/me/status/{status}")
    public ApiResponse<UpdateStatusResponse> updateStatus(
            @PathVariable String status,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.updateStatus(principalDetails.getId(), status));
    }

    // 프로필 이미지 업데이트
    @Operation(summary = ACCOUNT_UPDATE_PROFILE_IMAGE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/me/image")
    public ApiResponse<UpdateProfileImageUrlResponse> updateProfileImageUrl(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChangeProfileImageUrlRequest changeProfileImageUrlRequest) {
        return success(accountService.updateProfileImageUrl(principalDetails.getId(), changeProfileImageUrlRequest));
    }

    // 회원 탈퇴
    @Operation(summary = ACCOUNT_REVOKE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/revoke")
    public ApiResponse<RevokeResponse> revoke(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.revoke(principalDetails.getId()));
    }
}
