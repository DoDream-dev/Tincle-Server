package tinqle.tinqleServer.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.MyAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateNicknameResponse;
import tinqle.tinqleServer.domain.account.service.AccountService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    // 내 정보 조회
    @GetMapping("/me")
    public ApiResponse<MyAccountInfoResponse> getMyAccountInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getMyAccountInfo(principalDetails.getId()));
    }

    // 다른 유저 정보 조회
    @GetMapping("/{accountId}/profile")
    public ApiResponse<OthersAccountInfoResponse> getOthersAccountInfo(
            @PathVariable Long accountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getOthersAccountInfo(principalDetails.getId(), accountId));
    }

    // 코드 검색
    @GetMapping("/search/code/{code}")
    public ApiResponse<OthersAccountInfoResponse> searchCode(
            @PathVariable String code,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.searchByCode(principalDetails.getId(), code));
    }

    // 프로필 업데이트(nickname)
    @PutMapping("/me/nickname/{nickname}")
    public ApiResponse<UpdateNicknameResponse> updateNickname(
            @PathVariable String nickname,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.updateNickname(principalDetails.getId(), nickname));
    }

    // 프로필 업데이트(status)
    @PutMapping("/me/status/{status}")
    public ApiResponse<AccountResponseDto.UpdateStatusResponse> updateStatus(
            @PathVariable String status,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.updateStatus(principalDetails.getId(), status));
    }
}
