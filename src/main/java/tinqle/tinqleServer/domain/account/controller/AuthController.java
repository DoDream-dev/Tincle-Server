package tinqle.tinqleServer.domain.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.jwt.JwtDto;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.SocialLoginRequest;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.LoginMessageResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.LogoutResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.SignMessageResponse;
import tinqle.tinqleServer.domain.account.service.AuthService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;
import static tinqle.tinqleServer.domain.account.dto.request.AuthRequestDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid SocialLoginRequest socialLoginRequest) {
        LoginMessageResponse authMessage = authService.loginAccess(socialLoginRequest);
        return success(authMessage.detailData());
    }

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<JwtDto> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        SignMessageResponse signMessageResponse = authService.signUp(signUpRequest);
        return success(signMessageResponse.detaildata());
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(@Valid @RequestBody ReissueRequest reissueRequest) {
        return success(authService.reissue(reissueRequest));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(authService.logout(principalDetails.getId()));
    }
}
