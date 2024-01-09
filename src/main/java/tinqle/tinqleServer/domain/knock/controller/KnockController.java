package tinqle.tinqleServer.domain.knock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.knock.dto.response.KnockResponseDto.SendKnockResponse;
import tinqle.tinqleServer.domain.knock.service.KnockService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;
import static tinqle.tinqleServer.domain.knock.dto.request.KnockRequestDto.*;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_KNOCK, description = TAG_KNOCK_DESCRIPTION)
@RequestMapping("/knocks")
public class KnockController {

    private final KnockService knockService;

    @Operation(summary = SEND_KNOCK)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/send")
    public ApiResponse<SendKnockResponse> sendKnock(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody SendKnockRequest sendKnockRequest) {
        return success(knockService.sendKnock(principalDetails.getId(), sendKnockRequest));
    }
}
