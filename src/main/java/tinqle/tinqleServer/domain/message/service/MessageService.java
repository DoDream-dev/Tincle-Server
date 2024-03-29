package tinqle.tinqleServer.domain.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.message.dto.request.MessageRequest.SaveMessageRequest;
import tinqle.tinqleServer.domain.message.dto.response.ChatResponse;
import tinqle.tinqleServer.domain.message.dto.response.MessageCountResponse;
import tinqle.tinqleServer.domain.message.exception.MessageException;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.message.repository.MessageRepository;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto;
import tinqle.tinqleServer.domain.notification.service.FcmService;
import tinqle.tinqleServer.domain.room.model.Room;
import tinqle.tinqleServer.domain.room.model.Session;
import tinqle.tinqleServer.domain.room.repository.SessionRepository;
import tinqle.tinqleServer.domain.room.service.RoomService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final AccountService accountService;
    private final RoomService roomService;
    private final FcmService fcmService;
    private final FriendshipService friendshipService;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void createMessage(Long roomId, SaveMessageRequest saveMessageRequest) {
        Account sender = accountService.getAccountById(saveMessageRequest.accountId());
        Room room = roomService.getRoomById(roomId);

        Message message = messageRepository.save(handleMessage(sender, room, saveMessageRequest.content()));

        ChatResponse chatResponse = ChatResponse.of(roomId, message);
        messagingTemplate.convertAndSend("/queue/chat/rooms/" + roomId, chatResponse);

        sendFcm(message, sender, room);
    }

    public MessageCountResponse getMessageCount(Long accountId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Long count = messageRepository.countAllByReceiverAndIsReadFromReceiverIsFalse(loginAccount);
        return new MessageCountResponse(count);
    }

    private void sendFcm(Message message, Account sender, Room room) {
        Account receiver = message.getReceiver();
        Optional<Session> session = sessionRepository.findBySessionIdAndRoom(receiver.getSessionId(), room);
        if (session.isPresent())
            return;
        String nickname = friendshipService.getFriendNicknameSingle(receiver, sender);
        fcmService.sendPushMessage(receiver.getFcmToken(), NotificationDto.NotifyParams.ofSendMessage(message, nickname), 0L);
    }

    private Message handleMessage(Account sender, Room room, String content) {

        if (sender.getId().equals(room.getStarter().getId())) {
            room.activateRoom();
            return Message.of(sender, room.getFriend(), room, content);
        } else if (sender.getId().equals(room.getFriend().getId())) {
            room.activateRoom();
            return Message.of(sender, room.getStarter(), room, content);
        } else throw new MessageException(StatusCode.WRONG_ACCOUNT_IN_ROOM);
    }

}
