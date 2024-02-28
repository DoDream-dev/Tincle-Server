package tinqle.tinqleServer.domain.room.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.room.dto.request.RoomRequestDto.CreateRoomRequest;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.CreateRoomResponse;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.QuitRoomResponse;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.RoomCardResponse;
import tinqle.tinqleServer.domain.room.service.RoomService;

import java.util.List;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RequestMapping("/rooms")
@RestController
@Tag(name = TAG_ROOM, description = TAG_ROOM_DESCRIPTION)
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = CREATE_ROOM, description = CREATE_ROOM_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping
    public ApiResponse<CreateRoomResponse> createRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody CreateRoomRequest createRoomRequest) {
        return success(roomService.handleRoom(principalDetails.getId(), createRoomRequest));
    }

    @Operation(summary = GET_ROOMS)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping
    public ApiResponse<List<RoomCardResponse>> getRooms(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(roomService.getRooms(principalDetails.getId()));
    }

    @Operation(summary = QUIT_ROOM)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @DeleteMapping("/{roomId}")
    public ApiResponse<QuitRoomResponse> quitRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId) {
        return success(roomService.quitRoom(principalDetails.getId(), roomId));
    }
}
