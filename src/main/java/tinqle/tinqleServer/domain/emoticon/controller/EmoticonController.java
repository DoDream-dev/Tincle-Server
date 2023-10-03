package tinqle.tinqleServer.domain.emoticon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.emoticon.dto.request.EmoticonRequestDto.EmoticonReactRequest;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.EmoticonReactResponse;
import tinqle.tinqleServer.domain.emoticon.dto.response.EmoticonResponseDto.GetNicknameListResponse;
import tinqle.tinqleServer.domain.emoticon.service.EmoticonService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/emoticons")
public class EmoticonController {

    public final EmoticonService emoticonService;

    @GetMapping("/feeds/{feedId}")
    public ApiResponse<GetNicknameListResponse> getEmoticonReactAccounts(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(emoticonService.getEmoticonReactAccount(principalDetails.getId(), feedId));
    }

    @PutMapping("/feeds/{feedId}")
    public ApiResponse<EmoticonReactResponse> emoticonReact(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody EmoticonReactRequest emoticonReactRequest) {
        return success(emoticonService.emoticonReact(principalDetails.getId(), feedId, emoticonReactRequest));
    }
}
