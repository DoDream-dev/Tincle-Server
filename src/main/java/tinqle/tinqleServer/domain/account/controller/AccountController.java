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
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.MyAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateNicknameResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateStatusResponse;
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
    @Operation(summary = ACCOUNT_ME)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/me")
    public ApiResponse<MyAccountInfoResponse> getMyAccountInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getMyAccountInfo(principalDetails.getId()));
    }

    // 다른 유저 정보 조회
    @Operation(summary = ACCOUNT_OTHERS)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/{accountId}/profile")
    public ApiResponse<OthersAccountInfoResponse> getOthersAccountInfo(
            @PathVariable Long accountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getOthersAccountInfo(principalDetails.getId(), accountId));
    }

    // 코드 검색
    @Operation(summary = ACCOUNT_SEARCH_CODE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/search/code/{code}")
    public ApiResponse<OthersAccountInfoResponse> searchCode(
            @PathVariable String code,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.searchByCode(principalDetails.getId(), code));
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

    // 프로필 업데이트(status)
    @Operation(summary = ACCOUNT_UPDATE_STATUS)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/me/status/{status}")
    public ApiResponse<UpdateStatusResponse> updateStatus(
            @PathVariable String status,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.updateStatus(principalDetails.getId(), status));
    }

    // 회원 탈퇴
    @Operation(summary = ACCOUNT_REVOKE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/revoke")
    public ApiResponse<RevokeResponse> revoke(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.revoke(principalDetails.getId()));
    }
}
