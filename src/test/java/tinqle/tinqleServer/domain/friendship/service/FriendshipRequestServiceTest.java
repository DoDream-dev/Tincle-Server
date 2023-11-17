package tinqle.tinqleServer.domain.friendship.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.account.template.AccountTemplate;
import tinqle.tinqleServer.domain.friendship.dto.request.FriendshipRequestDto;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.FriendshipReqeustResponse;
import tinqle.tinqleServer.domain.friendship.exception.FriendshipException;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRequestRepository;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.BDDMockito.given;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipRequestTemplate.createDummyFriendshipRequest;

@ExtendWith(MockitoExtension.class)
public class FriendshipRequestServiceTest {

    @InjectMocks
    FriendshipRequestService friendshipRequestService;

    @Mock
    AccountService accountService;
    @Mock
    FriendshipRequestRepository requestRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    FriendshipRepository friendshipRepository;

    private static final Account dummyAccountA = AccountTemplate.createDummyAccountA();
    private static final Account dummyAccountB = AccountTemplate.createDummyAccountB();
    @Test
    @DisplayName("친구 요청 - 성공")
    public void friendshipRequestSuccess() throws Exception {
        //given
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(
                dummyAccountA,dummyAccountB, RequestStatus.WAITING)).willReturn(false);
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountA,dummyAccountB)).willReturn(false);

        //when
        FriendshipResponseDto.ResponseFriendship responseDto =
                friendshipRequestService.friendshipRequest(1L, new FriendshipRequestDto.RequestFriendship(2L, "하이"));

        //then
        assertThat(responseDto.friendshipRequestId()).isNull(); // 더 나은 확인 코드 추가 해야할 듯
    }

    @Test
    @DisplayName("친구 요청 - 실패(이미 신청한 상태)")
    public void friendshipRequestFailForAlreadyRequest() throws Exception {
        //given
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(
                dummyAccountA,dummyAccountB, RequestStatus.WAITING)).willReturn(true);
        //when - then
        assertThatThrownBy(() -> friendshipRequestService.friendshipRequest(1L,new FriendshipRequestDto.RequestFriendship(2L, "하이")))
                .isInstanceOf(FriendshipException.class)
                .hasMessage(StatusCode.DUPLICATE_FRIENDSHIP_REQUEST.getMessage());
    }

    @Test
    @DisplayName("친구 요청 - 실패(이미 친구 상태)")
    public void friendshipRequestFailForExistFriendship() throws Exception {
        //given
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.existsByRequestAccountAndResponseAccountAndRequestStatus(
                dummyAccountA,dummyAccountB, RequestStatus.WAITING)).willReturn(false);
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountA,dummyAccountB)).willReturn(true);

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.friendshipRequest(1L,new FriendshipRequestDto.RequestFriendship(2L, "하이")))
                .isInstanceOf(FriendshipException.class)
                .hasMessage(StatusCode.ALREADY_FRIENDSHIP.getMessage());
    }

    @Test
    @DisplayName("친구 요청 수락 - 성공")
    public void approveFriendshipRequestSuccess() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountB, dummyAccountA)).willReturn(false).willReturn(true);

        //when
        FriendshipReqeustResponse friendshipReqeustResponse = friendshipRequestService.approveFriendshipRequest(2L, 1L);

        //then
        assertThat(friendshipReqeustResponse.result()).isTrue();
    }

    @Test
    @DisplayName("친구 요청 수락 - 실패(받는 사람이 다름)") // 1이 2에게 요청했는데 1이 수락하는 상황
    public void approveFriendshipRequestFailForDifferentResponseAccount() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.approveFriendshipRequest(1L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("this friendship is wrong");
    }

    @Test
    @DisplayName("친구 요청 수락 - 실패(수락 및 거절을 이미 한 상태)")
    public void approveFriendshipRequestFailForNotWaitingStatus() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.APPROVE);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.approveFriendshipRequest(2L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("this friendship is wrong");
    }

    @Test
    @DisplayName("친구 요청 수락 - 실패(이미 친구 상태)")
    public void approveFriendshipRequestFailForAlreadyFriend() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountB, dummyAccountA)).willReturn(true);

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.approveFriendshipRequest(2L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("already exist friend relationship");
    }

    @Test
    @DisplayName("친구 요청 거절 - 성공")
    public void rejectFriendshipRequestSuccess() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountB, dummyAccountA)).willReturn(false).willReturn(true);

        //when
        FriendshipReqeustResponse friendshipReqeustResponse = friendshipRequestService.rejectFriendRequest(2L, 1L);

        //then
        assertThat(friendshipReqeustResponse.result()).isTrue();
    }

    @Test
    @DisplayName("친구 요청 거절 - 실패(받는 사람이 다름)")
    public void rejectFriendshipReqeustFailForDifferentResponseAccount() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.rejectFriendRequest(1L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("this friendship is wrong");
    }

    @Test
    @DisplayName("친구 요청 거절 - 실패(수락 및 거절을 이미 한 상태)")
    public void rejectFriendshipRequestFailForNotWaitingStatus() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.REJECT);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.rejectFriendRequest(2L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("this friendship is wrong");
    }

    @Test
    @DisplayName("친구 요청 거절 - 실패(이미 친구 상태)")
    public void rejectFriendshipRequestFailForAlreadyFriend() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountA, dummyAccountB, RequestStatus.WAITING);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));
        given(friendshipRepository.existsByAccountSelfAndAccountFriend(dummyAccountB, dummyAccountA)).willReturn(true);

        //when - then
        assertThatThrownBy(() -> friendshipRequestService.rejectFriendRequest(2L, 1L))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("already exist friend relationship");
    }

    @Test
    @DisplayName("친구 요청 메세지 조회 - 성공")
    public void getMessageSuccess() throws Exception {
        //given
        FriendshipRequest friendshipRequest = createDummyFriendshipRequest(dummyAccountB, dummyAccountA, RequestStatus.WAITING);
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(requestRepository.findById(1L)).willReturn(Optional.of(friendshipRequest));

        //when
        FriendshipResponseDto.FriendshipRequestMessageResponse message = friendshipRequestService.getMessage(1L, 1L);

        //then
        assertThat(message.message()).isEqualTo("안녕");
    }
}
