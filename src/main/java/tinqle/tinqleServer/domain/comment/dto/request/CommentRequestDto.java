package tinqle.tinqleServer.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static tinqle.tinqleServer.common.constant.ValidConstants.COMMENT_TEXT_LENGTH;

public class CommentRequestDto {
    public record CreateCommentRequest(
            @NotNull @Size(max = COMMENT_TEXT_LENGTH)
            String content) {}
}
