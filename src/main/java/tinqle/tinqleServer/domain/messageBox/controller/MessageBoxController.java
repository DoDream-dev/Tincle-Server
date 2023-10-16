package tinqle.tinqleServer.domain.messageBox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.messageBox.dto.request.MessageBoxRequestDto.CreateMessageBoxRequest;
import tinqle.tinqleServer.domain.messageBox.dto.response.MessageBoxResponseDto.CreateMessageBoxResponse;
import tinqle.tinqleServer.domain.messageBox.service.MessageBoxService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class MessageBoxController {

    private final MessageBoxService messageBoxService;

    @PostMapping("/{accountId}/message")
    public ApiResponse<CreateMessageBoxResponse> createMessageBox(
            @PathVariable Long accountId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateMessageBoxRequest createMessageBoxRequest) {
        return success(messageBoxService.createMessageBox(principalDetails.getId(), accountId, createMessageBoxRequest));
    }
}
