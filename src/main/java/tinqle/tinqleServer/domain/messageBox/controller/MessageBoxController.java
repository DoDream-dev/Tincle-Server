package tinqle.tinqleServer.domain.messageBox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.messageBox.dto.request.MessageBoxRequestDto.CreateMessageBoxRequest;
import tinqle.tinqleServer.domain.messageBox.dto.response.MessageBoxResponseDto.MessageBoxResponse;
import tinqle.tinqleServer.domain.messageBox.service.MessageBoxService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_MESSAGE_BOX, description = TAG_MESSAGE_BOX_DESCRIPTION)
@RequestMapping("/accounts")
public class MessageBoxController {

    private final MessageBoxService messageBoxService;

    @Operation(summary = MESSAGE_BOX_GET)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/me/message")
    public ApiResponse<SliceResponse<MessageBoxResponse>> getMessageBox(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(messageBoxService.getMessageBox(principalDetails.getId(), pageable, cursorId));
    }

    @Operation(summary = MESSAGE_BOX_CREATE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/{accountId}/message")
    public ApiResponse<MessageBoxResponse> createMessageBox(
            @PathVariable Long accountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateMessageBoxRequest createMessageBoxRequest) {
        return success(messageBoxService.createMessageBox(principalDetails.getId(), accountId, createMessageBoxRequest));
    }
}
