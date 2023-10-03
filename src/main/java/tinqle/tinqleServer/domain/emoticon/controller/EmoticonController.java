package tinqle.tinqleServer.domain.emoticon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.emoticon.dto.request.EmoticonRequestDto.EmoticonReactRequest;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.EmoticonReactResponse;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.GetNicknameListResponse;
import tinqle.tinqleServer.domain.emoticon.service.EmoticonService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@Tag(name = TAG_EMOTICON, description = TAG_EMOTICON_DESCRIPTION)
@RequestMapping("/emoticons")
public class EmoticonController {

    public final EmoticonService emoticonService;

    @Operation(summary = EMOTICON_GET_REACTION_ACCOUNT)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/feeds/{feedId}")
    public ApiResponse<GetNicknameListResponse> getEmoticonReactAccounts(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(emoticonService.getEmoticonReactAccount(principalDetails.getId(), feedId));
    }

    @Operation(summary = EMOTICON_REACT)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping("/feeds/{feedId}")
    public ApiResponse<EmoticonReactResponse> emoticonReact(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EmoticonReactRequest emoticonReactRequest) {
        return success(emoticonService.emoticonReact(principalDetails.getId(), feedId, emoticonReactRequest));
    }
}
