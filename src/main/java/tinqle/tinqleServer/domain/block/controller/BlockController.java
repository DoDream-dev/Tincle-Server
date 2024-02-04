package tinqle.tinqleServer.domain.block.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.block.service.BlockService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_BLOCK, description = TAG_BLOCK_DESCRIPTION)
@RequestMapping("/blocks")
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = BLOCK_ACCOUNT)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/{targetAccountId}")
    public ApiResponse<?> blockTargetAccount(
            @PathVariable Long targetAccountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(blockService.blockAccount(principalDetails.getId(), targetAccountId));
    }
}
