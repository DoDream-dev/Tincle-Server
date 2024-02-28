package tinqle.tinqleServer.domain.room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.room.dto.request.RoomRequestDto.CreateRoomRequest;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.CreateRoomResponse;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.QuitRoomResponse;
import tinqle.tinqleServer.domain.room.service.RoomService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RequestMapping("/rooms")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ApiResponse<CreateRoomResponse> createRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody CreateRoomRequest createRoomRequest) {
        return success(roomService.handleRoom(principalDetails.getId(), createRoomRequest));
    }

    @GetMapping
    public ApiResponse<?> getRooms(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(roomService.getRooms(principalDetails.getId()));
    }

    @DeleteMapping("/{roomId}")
    public ApiResponse<QuitRoomResponse> quitRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId) {
        return success(roomService.quitRoom(principalDetails.getId(), roomId));
    }
}
