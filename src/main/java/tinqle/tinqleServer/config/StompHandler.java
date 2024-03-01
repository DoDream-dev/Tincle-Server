package tinqle.tinqleServer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tinqle.tinqleServer.domain.room.service.EntryService;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final EntryService entryService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        if(accessor.getCommand() == StompCommand.CONNECT) {
            String loginAccountSocialEmail = getSocialEmailAndvalidateToken(accessor);
            entryService.enterSocket(loginAccountSocialEmail, sessionId);

        } else if(accessor.getCommand() == StompCommand.SUBSCRIBE) {
            String destination = accessor.getDestination();
            entryService.enterRoom(destination, sessionId);

        } else if (accessor.getCommand() == StompCommand.UNSUBSCRIBE) {
            entryService.quitRoom(sessionId);

        } else if (accessor.getCommand() == StompCommand.DISCONNECT) {
            entryService.quitSocket(sessionId);
        }

        return message;
    }

    private String getSocialEmailAndvalidateToken(StompHeaderAccessor accessor) {
        String accessToken = jwtProvider.resolveToken(accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION));

        if (accessToken == null || !jwtProvider.validate(accessToken))
            throw new AccountException(StatusCode.FILTER_ACCESS_DENIED);

        return jwtProvider.getSocialEmailAtSocket(accessToken);
    }
}
