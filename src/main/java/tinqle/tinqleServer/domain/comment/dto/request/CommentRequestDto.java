package tinqle.tinqleServer.domain.comment.dto.request;

public class CommentRequestDto {
    public record CreateCommentRequest(String content) {}
}
