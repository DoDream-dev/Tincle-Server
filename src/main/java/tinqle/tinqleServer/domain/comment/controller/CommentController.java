package tinqle.tinqleServer.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.comment.dto.request.CommentRequestDto.CreateCommentRequest;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CommentCardResponse;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CreateCommentResponse;
import tinqle.tinqleServer.domain.comment.service.CommentService;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{feedId}/comments")
    public ApiResponse<SliceResponse<CommentCardResponse>> getComments(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(commentService.getCommentsByFeed(principalDetails.getId(), feedId, pageable, cursorId));
    }

    @PostMapping("/{feedId}/comments/parents")
    public ApiResponse<CreateCommentResponse> createCommentParents(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            CreateCommentRequest createCommentRequest) {
        return success(commentService.createParentComment(principalDetails.getId(), feedId, createCommentRequest));
    }

}
