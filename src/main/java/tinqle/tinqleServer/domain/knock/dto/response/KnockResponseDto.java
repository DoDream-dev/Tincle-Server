package tinqle.tinqleServer.domain.knock.dto.response;

import lombok.Builder;
import tinqle.tinqleServer.domain.knock.model.Knock;

public class KnockResponseDto {

    public record SendKnockResponse(
            Long knockId,
            Long accountId,
            Long sendAccountId) {

        @Builder
        public SendKnockResponse {}

        public static SendKnockResponse of(Knock knock) {
            return SendKnockResponse.builder()
                    .accountId(knock.getAccount().getId())
                    .sendAccountId(knock.getSendAccount().getId())
                    .build();
        }
    }
}
