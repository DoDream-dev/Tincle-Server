package tinqle.tinqleServer.domain.friendship.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.friendship.dto.request.FriendshipRequestDto.RequestFriendship;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.CodeResponse;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.FriendshipReqeustResponse;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.ResponseFriendship;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping
    public ApiResponse<CodeResponse> getMyCode(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(friendshipService.getCode(principalDetails.getId()));
    }

    @PostMapping("/request")
    public ApiResponse<ResponseFriendship> friendshipRequest(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody RequestFriendship requestFriendship) {
        return success(friendshipService.friendshipRequest(principalDetails.getId(), requestFriendship));
    }

    @PostMapping("/request/{friendshipRequestId}/approval")
    public ApiResponse<FriendshipReqeustResponse> friendshipRequestApprove(
            @PathVariable Long friendshipRequestId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(friendshipService.approveFriendshipRequest(principalDetails.getId(), friendshipRequestId));
    }

    @PostMapping("/request/{friendshipRequestId}/reject")
    public ApiResponse<FriendshipReqeustResponse> friendshipRequestRefuse(
            @PathVariable Long friendshipRequestId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(friendshipService.rejectFriendRequest(principalDetails.getId(), friendshipRequestId));
    }

    @GetMapping("/manage")
    public ApiResponse<?> manage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(friendshipService.getFriendshipManage(principalDetails.getId(), pageable, cursorId));
    }

}
