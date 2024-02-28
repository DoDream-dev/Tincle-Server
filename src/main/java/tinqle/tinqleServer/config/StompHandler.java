package tinqle.tinqleServer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.config.jwt.JwtProvider;
import tinqle.tinqleServer.domain.account.exception.AccountException;

@Configuration
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() == StompCommand.CONNECT) {
            validateToken(accessor);
        }

        return message;
    }

    private void validateToken(StompHeaderAccessor accessor) {
        String accessToken = jwtProvider.resolveToken(accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION));

        if (accessToken == null)
            throw new AccountException(StatusCode.FILTER_ACCESS_DENIED);

        jwtProvider.validate(accessToken);
    }
}
