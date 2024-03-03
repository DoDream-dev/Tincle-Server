package tinqle.tinqleServer.domain.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.jwt.JwtDto;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.SocialLoginRequest;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.CheckVersionResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.LoginMessageResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.LogoutResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.SignMessageResponse;
import tinqle.tinqleServer.domain.account.service.AuthService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;
import static tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = TAG_AUTH, description = TAG_AUTH_DESCRIPTION)
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 로그인
    @Operation(summary = AUTH_LOGIN, description = AUTH_LOGIN_DESCRIPTION)
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid SocialLoginRequest socialLoginRequest) {
        LoginMessageResponse authMessage = authService.loginAccess(socialLoginRequest);
        return success(authMessage.detailData());
    }

    // 회원가입
    @Operation(summary = AUTH_SIGNUP)
    @PostMapping("/signup")
    public ApiResponse<JwtDto> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignMessageResponse signMessageResponse = authService.signUp(signUpRequest);
        return success(signMessageResponse.detaildata());
    }

    @Operation(summary = AUTH_VERSION_CHECK, description = AUTH_VERSION_CHECK_DESCRIPTION)
    @GetMapping("/version")
    public ApiResponse<CheckVersionResponse> checkVersion(
            @RequestParam String deviceType,
            @RequestParam String version) {
        return success(authService.checkVersion(deviceType, version));
    }

    // 토큰 재발급
    @Operation(summary = AUTH_REISSUE)
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(@Valid @RequestBody ReissueRequest reissueRequest) {
        return success(authService.reissue(reissueRequest));
    }

    // 로그아웃
    @Operation(summary = AUTH_LOGOUT)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(authService.logout(principalDetails.getId()));
    }
}
