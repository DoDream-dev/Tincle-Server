package tinqle.tinqleServer.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.config.security.PrincipalDetails;
import tinqle.tinqleServer.domain.comment.dto.request.CommentRequestDto.CreateCommentRequest;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.ChildCommentCard;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CommentCardResponse;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.CreateCommentResponse;
import tinqle.tinqleServer.domain.comment.dto.response.CommentResponseDto.DeleteCommentResponse;
import tinqle.tinqleServer.domain.comment.service.CommentService;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@Tag(name = TAG_COMMENT, description = TAG_COMMENT_DESCRIPTION)
@RequestMapping("/feeds")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = COMMENT_GET)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @GetMapping("/{feedId}/comments")
    public ApiResponse<SliceResponse<CommentCardResponse>> getComments(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) Long cursorId) {
        return success(commentService.getCommentsByFeed(principalDetails.getId(), feedId, pageable, cursorId));
    }

    @Operation(summary = PARENT_COMMENT_CREATE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/{feedId}/comments/parent")
    public ApiResponse<CreateCommentResponse> createCommentParent(
            @PathVariable Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        return success(commentService.createParentComment(principalDetails.getId(), feedId, createCommentRequest));
    }

    @Operation(summary = CHILD_COMMENT_CREATE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping("/{feedId}/comments/{parentId}/children")
    public ApiResponse<ChildCommentCard> createCommentChildren(
            @PathVariable Long feedId,
            @PathVariable Long parentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        return success(commentService.createChildComment(principalDetails.getId(), feedId, parentId, createCommentRequest));
    }

    @Operation(summary = COMMENT_DELETE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<DeleteCommentResponse> deleteParentComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(commentService.deleteComment(principalDetails.getId(), commentId));
    }
}
