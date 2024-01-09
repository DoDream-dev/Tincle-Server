package tinqle.tinqleServer.domain.knock.dto.request;

public class KnockRequestDto {

    public record SendKnockRequest(Long targetAccountId) {}
}
