package tinqle.tinqleServer.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.message.model.Message;
import tinqle.tinqleServer.domain.message.repository.MessageRepository;
import tinqle.tinqleServer.domain.room.dto.request.RoomRequestDto.CreateRoomRequest;
import tinqle.tinqleServer.domain.room.dto.response.RoomResponseDto.*;
import tinqle.tinqleServer.domain.room.exception.RoomException;
import tinqle.tinqleServer.domain.room.model.Room;
import tinqle.tinqleServer.domain.room.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final AccountService accountService;
    private final FriendshipService friendshipService;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final FriendshipRepository friendshipRepository;


    public List<RoomCardResponse> getRooms(Long accountId) {
        Account loginAccount = accountService.getAccountById(accountId);

        List<Room> rooms = roomRepository.findAllByAccountAndIsNotDeletedSortRecently(accountId);
        List<Friendship> friendships = friendshipRepository
                .findAllByAccountSelfAndIsChangeFriendNickname(loginAccount.getId(), true);

        return rooms.stream()
                .filter(room -> messageRepository.findTop1ByRoomOrderByIdDesc(room).isPresent())
                .map(room -> {
                    Account targetAccount = getTargetAccount(room, loginAccount);
                    String nickname = friendshipService.getFriendNickname(friendships, targetAccount);
                    Long unreadCount = getUnreadCountByRoom(room, loginAccount);
                    Message message = getTop1ByRoomOrderByIdDesc(room);    // 위에서 검증 완료
                    return RoomCardResponse.of(targetAccount, nickname, room, unreadCount, message);
                }).toList();
    }

    private Message getTop1ByRoomOrderByIdDesc(Room room) {
        return messageRepository.findTop1ByRoomOrderByIdDesc(room).orElseThrow(() -> new RoomException(StatusCode.MESSAGE_ERROR));
    }

    private Long getUnreadCountByRoom(Room room, Account account) {

        return messageRepository.countAllByReceiverAndRoomAndIsReadFromReceiverIsFalse(account, room);
    }

    public GetAccountInfoResponse getAccountInfo(Long accountId, Long roomId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Room room = getRoomById(roomId);

        Account receiver = getTargetAccount(room, loginAccount);
        OthersAccountInfoResponse othersAccountInfo = accountService.getOthersAccountInfo(accountId, receiver.getId());

        return new GetAccountInfoResponse(othersAccountInfo, loginAccount.getId());
    }

    public SliceResponse<MessageCardResponse> getMessages(Long accountId, Long roomId, Pageable pageable, Long cursorId) {
        Account loginAccount = accountService.getAccountById(accountId);
        Room room = getRoomById(roomId);

        Slice<Message> messages = messageRepository.findByRoomSortRecently(roomId, pageable, cursorId, room.isStarter(loginAccount));
        Slice<MessageCardResponse> result =
                messages.map(message -> MessageCardResponse.of(message, message.isAuthor(loginAccount)));

        return SliceResponse.of(result);

    }

    private Account getTargetAccount(Room room, Account loginAccount) {
        if (room.getStarter().getId().equals(loginAccount.getId()))
            return room.getFriend();
        return room.getStarter();
    }

    @Transactional
    public CreateRoomResponse handleRoom(Long accountId, CreateRoomRequest createRoomRequest) {

        Account loginAccount = accountService.getAccountById(accountId);
        Account targetAccount = accountService.getAccountById(createRoomRequest.targetAccountId());

        Room room = getRoomIdAndCheckExistRoom(loginAccount, targetAccount);

        return new CreateRoomResponse(room.getId(), room.getCreatedAt().toString());
    }

    private Room getRoomIdAndCheckExistRoom(Account loginAccount, Account targetAccount) {

        Optional<Room> roomOptionalFirstCase = roomRepository.findByStarterAndFriend(loginAccount, targetAccount);
        Optional<Room> roomOptionalSecondCase = roomRepository.findByStarterAndFriend(targetAccount, loginAccount);

        return roomOptionalFirstCase
                .orElseGet(() -> roomOptionalSecondCase
                        .orElseGet(() -> {
                            Room room = Room.of(loginAccount, targetAccount);
                            return roomRepository.save(room);
                        }));

    }

    @Transactional
    public QuitRoomResponse quitRoom(Long accountId, Long roomId) {
        Account loginAccount = accountService.getAccountById(accountId);

        Room room = getRoomById(roomId);

        room.quit(loginAccount);

        if (room.isStarter(loginAccount)) {
            messageRepository.deleteAllMessageWhenStarter(loginAccount, room);
        } else {
            messageRepository.deleteAllMessageWhenFriend(loginAccount, room);
        }

        return new QuitRoomResponse(true);
    }

    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new RoomException(StatusCode.NOT_FOUND_ROOM));
    }
}
