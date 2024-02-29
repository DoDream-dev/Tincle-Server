package tinqle.tinqleServer.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.room.exception.RoomException;
import tinqle.tinqleServer.domain.room.model.Room;
import tinqle.tinqleServer.domain.room.model.Session;
import tinqle.tinqleServer.domain.room.repository.SessionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntryService {

    private final AccountRepository accountRepository;
    private final RoomService roomService;
    private final SessionRepository sessionRepository;

    @Transactional
    public void enterSocket(String socialEmail, String sessionId) {
        Account loginAccount = accountRepository.findBySocialEmail(socialEmail)
                .orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));

        loginAccount.updateSessionId(sessionId);
    }

    @Transactional
    public void enterRoom(String destination, String sessionId) {
        Account loginAccount = accountRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));

        Long roomId = Long.valueOf(destination.split("/")[4]);
        Room room = roomService.getRoomById(roomId);

        Optional<Session> sessionOptional = sessionRepository.findByAccountAndRoom(loginAccount, room);

        if (sessionOptional.isEmpty()) {
            Session session = Session.of(loginAccount, room, sessionId);
            sessionRepository.save(session);
        } else {
            Session session = sessionOptional.get();
            session.updateSessionId(sessionId);
        }

    }

    @Transactional
    public void quitRoom(String sessionId) {
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RoomException(StatusCode.NOT_FOUND_SESSION));

        session.updateSessionId("");
    }

    @Transactional
    public void quitSocket(String sessionId) {
        Account loginAccount = accountRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));

        loginAccount.updateSessionId("");
    }

}
