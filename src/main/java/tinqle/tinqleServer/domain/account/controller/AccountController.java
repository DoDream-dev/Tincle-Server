package tinqle.tinqleServer.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.AccountInfoResponse;
import tinqle.tinqleServer.domain.account.service.AccountService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("me")
    public ApiResponse<AccountInfoResponse> getAccountInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(accountService.getAccountInfo(principalDetails.getId()));
    }
}
